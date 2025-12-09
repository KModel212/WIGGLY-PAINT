package controller;

import brush.EraserBrush;
import brush.Paintable;
import canvas.CanvasData;
import gui.CanvasPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;
import utils.themes.ThemeManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasController {

    private final CanvasPane pane;
    private final CanvasData data;
    private final BrushController brushController;   // ★ NEW ★

    private double lastX, lastY;
    private long lastTime;
    private boolean drawing = false;

    // throttle settings
    private long lastDrawTime = 0;
    private final long DRAG_DELAY_NS = 20_000_000;  // 20 ms
    private final double minDistance = 1.1;

    public CanvasController(CanvasPane pane, CanvasData data, BrushController brushController) {
        this.pane = pane;
        this.data = data;
        this.brushController = brushController;   // ★ inject brush controller

        attachEvents();

        // Pre-render both wiggle frames
        redraw(1);
        redraw(2);
        ThemeManager.addListener(() -> {
            // Re-render pixel colors for FG/PRIMARY/SECONDARY/ACCENT
            redraw(1);
            redraw(2);
        });
    }

    private void attachEvents() {

        // =======================
        // Mouse Press
        // =======================
        pane.layer3.setOnMousePressed(e -> {

            drawing = true;

            Paintable tool = brushController.getActiveBrush();
            if (tool != null) {
                tool.resetStroke();      // ★ THIS FIXES THE CONTINUOUS-LINE BUG
            }

            lastX = e.getX();
            lastY = e.getY();
            lastTime = System.nanoTime();

            if (tool != null) {
                tool.paintOnEveryLayer(data, lastX, lastY, 0);
            }

            redraw(1);
            redraw(2);
        });


        // =======================
        // Mouse Drag
        // =======================
        pane.layer3.setOnMouseDragged(e -> {

            if (!drawing) return;

            double ix = e.getX();
            double iy = e.getY();

            Paintable tool = brushController.getActiveBrush();

            if (tool instanceof EraserBrush eraser) {
                drawCursorCircle(ix, iy, eraser.getBaseSize());   // <— FIX
            }

            long now = System.nanoTime();

            if (now - lastDrawTime < DRAG_DELAY_NS) return;
            lastDrawTime = now;

            double x = e.getX();
            double y = e.getY();

            double dx = x - lastX;
            double dy = y - lastY;

            if (dx * dx + dy * dy < minDistance * minDistance) return;

            double dt = (now - lastTime) / 1e9;
            double speed = Math.sqrt(dx * dx + dy * dy) / dt;

            if (tool != null) {
                tool.paintOnEveryLayer(data, x, y, speed);
            }

            lastX = x;
            lastY = y;
            lastTime = now;

            redraw(1);
            redraw(2);
        });

        pane.layer3.setOnMouseReleased(e -> drawing = false);

        pane.layer3.setOnMouseMoved(e -> {
            double ix = e.getX();
            double iy = e.getY();

            Paintable tool = brushController.getActiveBrush();

            if (tool instanceof EraserBrush eraser) {
                drawCursorCircle(ix, iy, eraser.getBaseSize());
            } else {
                pane.clearCursor();
            }
        });

        pane.layer3.setOnMouseExited(e -> {
            pane.clearCursor();
        });

    }


    // ==========================
    // RENDER
    // ==========================
    public void redraw(int layerIndex) {

        Canvas layer = switch (layerIndex) {
            case 0 -> pane.layer0;
            case 1 -> pane.layer1;
            case 2 -> pane.layer2;
            case 3 -> pane.layer3;
            default -> throw new IllegalArgumentException("Invalid layer index");
        };

        GraphicsContext gc = layer.getGraphicsContext2D();
        int size = pane.getInternalSize();

        // -----------------------------
        // LAYER 3 MUST BE transparent!
        // -----------------------------
        if (layerIndex == 3) {
            gc.clearRect(0, 0, size, size);  // transparent
            return;                          // NO BORDER, NO PIXELS
        }
        // -----------------------------

        byte[][] pixels = switch (layerIndex) {
            case 0 -> data.raw();
            case 1 -> data.A();
            case 2 -> data.B();
            default -> null;
        };

        if (pixels == null) return;

        gc.clearRect(0, 0, size, size);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int idx = pixels[x][y];
                gc.setFill(
                        switch (idx) {
                            case CanvasData.FG -> ThemeManager.get().fg;
                            case CanvasData.PRIMARY -> ThemeManager.get().primary;
                            case CanvasData.SECONDARY -> ThemeManager.get().secondary;
                            case CanvasData.ACCENT -> ThemeManager.get().accent;
                            default -> ThemeManager.get().bg;
                        }
                );
                gc.fillRect(x, y, 1, 1);
            }
        }

        // Border for base layers
        gc.setStroke(ThemeManager.get().fg);
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, size, size);
    }


    private void drawCursorCircle(double x, double y, int radius) {
        var gc = pane.getCursorLayer().getGraphicsContext2D();
        int size = pane.getInternalSize();

        // clear previous cursor
        gc.clearRect(0, 0, size, size);

        // pixel color
        gc.setFill(Color.BLACK);

        int cx = (int) x;
        int cy = (int) y;

        // rasterized circle (outline only)
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {

                int distSq = dx*dx + dy*dy;

                // check if pixel is near the circle boundary
                if (Math.abs(distSq - radius*radius) < radius * 1.5) {
                    int px = cx + dx;
                    int py = cy + dy;

                    gc.fillRect(px, py, 1, 1); // draw pixel
                }
            }
        }
    }



    // ==========================
    // WIGGLE LOOP
    // ==========================
    private int wiggleFrame = 0;

    public void startWiggleLoop() {

        Timeline t = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    wiggleFrame = 1 - wiggleFrame;

                    if (wiggleFrame == 0) {
                        pane.layer1.toFront(); // frame A
                    } else {
                        pane.layer2.toFront(); // frame B
                    }

                    pane.layer3.toFront(); // input layer always on top
                })
        );

        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }
}
