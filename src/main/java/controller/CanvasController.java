package controller;

import brush.Brushable;
import brush.PencilBrush;
import canvas.CanvasData;
import gui.CanvasPane;
import utils.themes.ThemeManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasController {

    private final CanvasPane pane;
    private final CanvasData data;

    private double lastX, lastY;
    private long lastTime;
    private boolean drawing = false;

    private Brushable tool = new PencilBrush(); // default tool

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
        redraw();
    }

    private void attachEvents() {

        // Mouse Pressed
        pane.layer2.setOnMousePressed(e -> {

            System.out.println("PRESS at " + e.getX() + ", " + e.getY());

            drawing = true;
            lastX = e.getX();
            lastY = e.getY();
            lastTime = System.nanoTime();

            tool.paint(data, lastX, lastY, 0);
            redraw();
        });


        // Mouse Dragged
        pane.layer2.setOnMouseDragged(e -> {

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

            redraw();
        });


        // Mouse Released
        pane.layer2.setOnMouseReleased(e -> {
            drawing = false;
            System.out.println("RELEASE at " + e.getX() + ", " + e.getY());
        });
    }


    // -------------------------
    // RENDERING
    // -------------------------
    public void redraw() {
        GraphicsContext gc = pane.layer0.getGraphicsContext2D();
        byte[][] pixels = data.raw();

        int size = pane.getCanvasSize();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int idx = pixels[x][y];

                gc.setFill(switch (idx) {
                    case CanvasData.FG       -> ThemeManager.get().fg;
                    case CanvasData.PRIMARY  -> ThemeManager.get().primary;
                    case CanvasData.SECONDARY-> ThemeManager.get().secondary;
                    case CanvasData.ACCENT   -> ThemeManager.get().accent;
                    default                  -> ThemeManager.get().bg;
                });

                gc.fillRect(x, y, 1, 1);
            }
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeRect(0, 0, size, size);
    }
}
