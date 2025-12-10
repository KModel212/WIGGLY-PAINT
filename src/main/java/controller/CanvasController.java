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

/**
 * Controls all canvas interaction:
 * <ul>
 *     <li>Handles mouse input (press, drag, release)</li>
 *     <li>Calls the active brush to paint on {@link CanvasData}</li>
 *     <li>Redraws pixel buffers onto JavaFX canvas layers</li>
 *     <li>Draws cursor previews for the eraser</li>
 *     <li>Runs the wiggle animation loop (layer swapping)</li>
 * </ul>
 *
 * This controller manages all painting logic between the UI (CanvasPane)
 * and the pixel buffer (CanvasData).
 */
public class CanvasController {

    // ============================================================
    // Fields
    // ============================================================

    /** Pane containing all layered canvases (0–3). */
    private final CanvasPane pane;

    /** Pixel buffer used by brushes. */
    private final CanvasData data;

    /** Provides access to the currently selected brush. */
    private final BrushController brushController;

    // Stroke tracking values
    private double lastX, lastY;
    private long lastTime;
    private boolean drawing = false;

    // Drag throttling
    private long lastDrawTime = 0;
    private static final long DRAG_DELAY_NS = 20_000_000; // 20ms
    private static final double MIN_DISTANCE = 1.1;

    // Wiggle frame index
    private int wiggleFrame = 0;


    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Creates a canvas controller, attaches mouse events, prepares
     * wiggle frames, and registers theme-change listeners.
     *
     * @param pane            UI canvas container (multiple layers)
     * @param data            framebuffer storage
     * @param brushController brush selection controller
     */
    public CanvasController(CanvasPane pane, CanvasData data, BrushController brushController) {

        this.pane = pane;
        this.data = data;
        this.brushController = brushController;

        attachEvents();

        // Pre-render wiggle layers
        redraw(1);
        redraw(2);

        // Re-render on theme change
        ThemeManager.addListener(() -> {
            redraw(1);
            redraw(2);
        });
    }


    // ============================================================
    // EVENT HANDLERS
    // ============================================================

    /**
     * Attaches all mouse event listeners to the top input layer (layer3).
     * Handles:
     * <ul>
     *     <li>Press → begin a stroke</li>
     *     <li>Drag → paint with active brush</li>
     *     <li>Release → end stroke</li>
     *     <li>Move → show eraser preview</li>
     * </ul>
     */
    private void attachEvents() {

        // ------------------------------------------------------------
        // MOUSE PRESS — start new stroke
        // ------------------------------------------------------------
        pane.layer3.setOnMousePressed(e -> {
            drawing = true;

            Paintable tool = brushController.getActiveBrush();
            if (tool != null) tool.resetStroke(); // prevents line-jump

            lastX = e.getX();
            lastY = e.getY();
            lastTime = System.nanoTime();

            if (tool != null) tool.paintOnEveryLayer(data, lastX, lastY, 0);

            redraw(1);
            redraw(2);
        });


        // ------------------------------------------------------------
        // MOUSE DRAG — draw continuous stroke
        // ------------------------------------------------------------
        pane.layer3.setOnMouseDragged(e -> {
            if (!drawing) return;

            double x = e.getX();
            double y = e.getY();
            Paintable tool = brushController.getActiveBrush();

            // Eraser cursor preview
            if (tool instanceof EraserBrush eraser) {
                drawCursorCircle(x, y, eraser.getBaseSize());
            }

            long now = System.nanoTime();
            if (now - lastDrawTime < DRAG_DELAY_NS) return;
            lastDrawTime = now;

            double dx = x - lastX;
            double dy = y - lastY;

            // Filter tiny movements (prevents over-densely packed dabs)
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
        // MOUSE RELEASE — end stroke
        // ------------------------------------------------------------
        pane.layer3.setOnMouseReleased(e -> drawing = false);


        // ------------------------------------------------------------
        // CURSOR PREVIEW — only for eraser
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

    /**
     * Renders one framebuffer layer (0,1,2, or 3) into its JavaFX Canvas.
     * <p>
     * Layer meanings:
     * <ul>
     *     <li>0 → base pixels</li>
     *     <li>1 → wiggle frame A</li>
     *     <li>2 → wiggle frame B</li>
     *     <li>3 → cursor preview</li>
     * </ul>
     *
     * @param layerIndex framebuffer index to redraw
     */
    private void redraw(int layerIndex) {

        Canvas layer = switch (layerIndex) {
            case 0 -> pane.layer0;
            case 1 -> pane.layer1;
            case 2 -> pane.layer2;
            case 3 -> pane.layer3;
            default -> throw new IllegalArgumentException("Invalid layer index");
        };

        GraphicsContext gc = layer.getGraphicsContext2D();
        int size = pane.getInternalSize();

        byte[][] pixels = switch (layerIndex) {
            case 0 -> data.raw();
            case 1 -> data.A();
            case 2 -> data.B();
            default -> null;
        };

        if (pixels == null) return;

        gc.clearRect(0, 0, size, size);

        // Draw pixels as 1×1 rectangles (pixel-art style)
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

        // Draw canvas border
        gc.setStroke(ThemeManager.get().fg);
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, size, size);
    }


    // ============================================================
    // CURSOR (eraser preview)
    // ============================================================

    /**
     * Draws a pixelated outline circle used to preview the eraser radius.
     *
     * @param x      center X
     * @param y      center Y
     * @param radius eraser radius
     */
    private void drawCursorCircle(double x, double y, int radius) {
        GraphicsContext gc = pane.getCursorLayer().getGraphicsContext2D();
        int size = pane.getInternalSize();

        gc.clearRect(0, 0, size, size);
        gc.setFill(ThemeManager.get().fg);

        int cx = (int) x;
        int cy = (int) y;

        // Rasterized circle outline
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

    /**
     * Starts the wiggle animation loop.
     * <ul>
     *     <li>Toggles between wiggle frame A and B every 100ms</li>
     *     <li>Brings cursor layer to front</li>
     *     <li>Creates an animated shaky drawing effect</li>
     * </ul>
     */
    public void startWiggleLoop() {
        Timeline t = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    wiggleFrame = 1 - wiggleFrame;

                    if (wiggleFrame == 0)
                        pane.layer1.toFront();
                    else
                        pane.layer2.toFront();

                    // Cursor layer always stays on top
                    pane.layer3.toFront();
                })
        );
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }
}
