package controller;

import brush.Brushable;
import brush.MarkerPen;
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

    private double lastX, lastY;
    private long lastTime;
    private boolean drawing = false;

    private Brushable tool = new MarkerPen(10,0); // default tool

    // -------------------------
    // DELAY SETTINGS (move them OUTSIDE the method)
    // -------------------------
    private long lastDrawTime = 0;
    private final long DRAG_DELAY_NS = 6_000_000; // 6 ms throttle
    private final double minDistance = 1.1;       // minimum movement
    // -------------------------

    public CanvasController(CanvasPane pane, CanvasData data) {
        this.pane = pane;
        this.data = data;
        attachEvents();
        redraw(1);
        redraw(2);
    }

    private void attachEvents() {

        // Mouse Pressed
        pane.layer3.setOnMousePressed(e -> {

            System.out.println("PRESS at " + e.getX() + ", " + e.getY());

            drawing = true;
            lastX = e.getX();
            lastY = e.getY();
            lastTime = System.nanoTime();

            tool.paint(data, lastX, lastY, 0);

            redraw(1);
            redraw(2);
        });


        // Mouse Dragged
        pane.layer3.setOnMouseDragged(e -> {

            if (!drawing) return;

            long now = System.nanoTime();

            // throttle (6ms)
            if (now - lastDrawTime < DRAG_DELAY_NS) return;
            lastDrawTime = now;

            double x = e.getX();
            double y = e.getY();

            double dx = x - lastX;
            double dy = y - lastY;

            // skip tiny movements
            if (dx * dx + dy * dy < minDistance * minDistance) return;

            // speed calculation
            double dt = (now - lastTime) / 1e9;
            double speed = Math.sqrt(dx * dx + dy * dy) / dt;

            // draw
            tool.paint(data, x, y, speed);

            lastX = x;
            lastY = y;
            lastTime = now;

            redraw(1);
            redraw(2);
        });


        // Mouse Released
        pane.layer3.setOnMouseReleased(e -> {
            drawing = false;
            System.out.println("RELEASE at " + e.getX() + ", " + e.getY());
        });
    }


    // -------------------------
    // RENDERING
    // -------------------------
    public void redraw(int layerIndex) {

        // pick the correct canvas
        Canvas layer = switch (layerIndex) {
            case 0 -> pane.layer0;
            case 1 -> pane.layer1;
            case 2 -> pane.layer2;
            case 3 -> pane.layer3;
            default -> throw new IllegalArgumentException("Invalid layer index: " + layerIndex);
        };

        GraphicsContext gc = layer.getGraphicsContext2D();

        // choose the pixel buffer depending on layer
        byte[][] pixels = switch (layerIndex) {
            case 0 -> data.raw(); // background or main pixels
            case 1 -> data.A();   // wiggle frame A
            case 2 -> data.B();   // wiggle frame B
            case 3 -> data.raw(); // usually used only for overlay
            default -> data.raw();
        };

        int size = pane.getInternalSize();

        gc.clearRect(0, 0, size, size);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int idx = pixels[x][y];

                gc.setFill(switch (idx) {
                    case CanvasData.FG        -> ThemeManager.get().fg;
                    case CanvasData.PRIMARY   -> ThemeManager.get().primary;
                    case CanvasData.SECONDARY -> ThemeManager.get().secondary;
                    case CanvasData.ACCENT    -> ThemeManager.get().accent;
                    default                   -> ThemeManager.get().bg;
                });

                gc.fillRect(x, y, 1, 1);
            }
        }

        // Border for clarity (if needed)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, size, size);

    }

    private int wiggleFrame = 0;

    public void startWiggleLoop() {
        Timeline t = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    wiggleFrame = 1 - wiggleFrame;

                    if (wiggleFrame == 0) {
                        pane.layer1.toFront();  // show A
                    } else {
                        pane.layer2.toFront();  // show B
                    }

                    // Always keep input layer (layer3) on top
                    pane.layer3.toFront();
                })
        );

        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }


}
