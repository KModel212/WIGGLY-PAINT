package controller;

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

            Paintable tool = brushController.getActiveBrush();   // ★ updated
            if (tool != null) {
                tool.paintOnEveryLayer(data, x, y, speed);
            }

            lastX = x;
            lastY = y;
            lastTime = now;

            redraw(1);
            redraw(2);
        });


        // =======================
        // Mouse Release
        // =======================
        pane.layer3.setOnMouseReleased(e -> drawing = false);
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
            default -> throw new IllegalArgumentException("Invalid layer index: " + layerIndex);
        };

        GraphicsContext gc = layer.getGraphicsContext2D();

        byte[][] pixels = switch (layerIndex) {
            case 0 -> data.raw();
            case 1 -> data.A();
            case 2 -> data.B();
            case 3 -> data.raw();
            default -> data.raw();
        };

        int size = pane.getInternalSize();

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

        // border
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, size, size);
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
