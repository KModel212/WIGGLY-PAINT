package controller;

import brush.EraserBrush;
import brush.Paintable;
import canvas.CanvasData;
import gui.CanvasPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import utils.themes.ThemeManager;

public class CanvasController {

    // ============================================================
    // Fields
    // ============================================================
    private final CanvasPane pane;
    private final CanvasData data;
    private final BrushController brushController;

    // Stroke tracking
    private double lastX, lastY;
    private long lastTime;
    private boolean drawing = false;

    // Throttle settings
    private long lastDrawTime = 0;
    private static final long DRAG_DELAY_NS = 20_000_000; // 20 ms
    private static final double MIN_DISTANCE = 1.1;

    // Wiggle
    private int wiggleFrame = 0;


    // ============================================================
    // Constructor
    // ============================================================
    public CanvasController(CanvasPane pane, CanvasData data, BrushController brushController) {

        this.pane = pane;
        this.data = data;
        this.brushController = brushController;

        attachEvents();

        // Pre-render both wiggle frames
        redraw(1);
        redraw(2);

        // Re-render when theme changes
        ThemeManager.addListener(() -> {
            redraw(1);
            redraw(2);
        });
    }


    // ============================================================
    // EVENT HANDLERS
    // ============================================================
    private void attachEvents() {

        // ------------------------------------------------------------
        // Mouse Press
        // ------------------------------------------------------------
        pane.layer3.setOnMousePressed(e -> {

            drawing = true;

            Paintable tool = brushController.getActiveBrush();
            if (tool != null) tool.resetStroke();  // prevents line-jump

            lastX = e.getX();
            lastY = e.getY();
            lastTime = System.nanoTime();

            if (tool != null) tool.paintOnEveryLayer(data, lastX, lastY, 0);

            redraw(1);
            redraw(2);
        });


        // ------------------------------------------------------------
        // Mouse Drag
        // ------------------------------------------------------------
        pane.layer3.setOnMouseDragged(e -> {

            if (!drawing) return;

            double x = e.getX();
            double y = e.getY();

            Paintable tool = brushController.getActiveBrush();

            // Eraser circle preview
            if (tool instanceof EraserBrush eraser) {
                drawCursorCircle(x, y, eraser.getBaseSize());
            }

            long now = System.nanoTime();
            if (now - lastDrawTime < DRAG_DELAY_NS) return;
            lastDrawTime = now;

            double dx = x - lastX;
            double dy = y - lastY;

            // avoid spamming points too close
            if (dx * dx + dy * dy < MIN_DISTANCE * MIN_DISTANCE) return;

            double dt = (now - lastTime) / 1e9;
            double speed = Math.sqrt(dx * dx + dy * dy) / dt;

            if (tool != null) tool.paintOnEveryLayer(data, x, y, speed);

            lastX = x;
            lastY = y;
            lastTime = now;

            redraw(1);
            redraw(2);
        });


        // ------------------------------------------------------------
        // Mouse Release
        // ------------------------------------------------------------
        pane.layer3.setOnMouseReleased(e -> drawing = false);


        // ------------------------------------------------------------
        // Cursor Preview (eraser only)
        // ------------------------------------------------------------
        pane.layer3.setOnMouseMoved(e -> {

            Paintable tool = brushController.getActiveBrush();

            if (tool instanceof EraserBrush eraser) {
                drawCursorCircle(e.getX(), e.getY(), eraser.getBaseSize());
            } else {
                pane.clearCursor();
            }
        });

        pane.layer3.setOnMouseExited(e -> pane.clearCursor());
    }


    // ============================================================
    // RENDER
    // ============================================================
    public void redraw(int layerIndex) {

        Canvas layer = switch (layerIndex) {
            case 0 -> pane.layer0;
            case 1 -> pane.layer1;
            case 2 -> pane.layer2;
            case 3 -> pane.layer3; // cursor/input layer
            default -> throw new IllegalArgumentException("Invalid layer index");
        };

        GraphicsContext gc = layer.getGraphicsContext2D();
        int size = pane.getInternalSize();

        // ------------------------------------------------------------
        // LAYER 3: transparent input layer
        // ------------------------------------------------------------
        if (layerIndex == 3) {
            gc.clearRect(0, 0, size, size);
            return;
        }

        byte[][] pixels = switch (layerIndex) {
            case 0 -> data.raw();
            case 1 -> data.A();
            case 2 -> data.B();
            default -> null;
        };

        if (pixels == null) return;

        gc.clearRect(0, 0, size, size);

        // Draw pixels
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                int idx = pixels[x][y];

                gc.setFill(
                        switch (idx) {
                            case CanvasData.FG        -> ThemeManager.get().fg;
                            case CanvasData.PRIMARY   -> ThemeManager.get().primary;
                            case CanvasData.SECONDARY -> ThemeManager.get().secondary;
                            case CanvasData.ACCENT    -> ThemeManager.get().accent;
                            default                   -> ThemeManager.get().bg;
                        }
                );

                gc.fillRect(x, y, 1, 1);
            }
        }

        // Border for visible layers
        gc.setStroke(ThemeManager.get().fg);
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, size, size);
    }


    // ============================================================
    // CURSOR (Eraser preview)
    // ============================================================
    private void drawCursorCircle(double x, double y, int radius) {

        GraphicsContext gc = pane.getCursorLayer().getGraphicsContext2D();
        int size = pane.getInternalSize();

        gc.clearRect(0, 0, size, size);
        gc.setFill(Color.BLACK);

        int cx = (int) x;
        int cy = (int) y;

        // rasterized outline circle
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {

                int distSq = dx * dx + dy * dy;
                if (Math.abs(distSq - radius * radius) < radius * 1.5) {
                    gc.fillRect(cx + dx, cy + dy, 1, 1);
                }
            }
        }
    }


    // ============================================================
    // WIGGLE LOOP
    // ============================================================
    public void startWiggleLoop() {

        Timeline t = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {

                    wiggleFrame = 1 - wiggleFrame;

                    if (wiggleFrame == 0)
                        pane.layer1.toFront();
                    else
                        pane.layer2.toFront();

                    pane.layer3.toFront(); // always on top (cursor)
                })
        );

        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }
}
