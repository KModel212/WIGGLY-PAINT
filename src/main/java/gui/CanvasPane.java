package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import utils.themes.ThemeManager;

/**
 * A multi-layered pixel-art canvas for WigglyPaint.
 * <p>
 * The CanvasPane provides four stacked layers:
 * <ul>
 *     <li><b>layer0</b> – background + border</li>
 *     <li><b>layer1</b> – wiggle frame A</li>
 <li><b>layer2</b> – wiggle frame B</li>
 *     <li><b>layer3</b> – cursor / input layer</li>
 * </ul>
 * Internally, all painting occurs on a 200×200 logical pixel grid
 * (for crisp pixel art). The entire StackPane is then scaled to
 * 500×500 for display while preserving nearest-neighbor sharpness.
 * <p>
 * CanvasPane also handles theme updates and provides coordinate
 * conversion between displayed pixels and internal pixel coordinates.
 */
public class CanvasPane extends Pane {

    // ============================================================
    // Canvas sizes
    // ============================================================

    /** Logical pixel resolution of the canvas (painting resolution). */
    private static final int internalSize = 200;

    /** Scaled size displayed on screen (UI resolution). */
    private static final int displaySize = 500;


    // ============================================================
    // Layers
    // ------------------------------------------------------------
    // layer0 → background (theme-colored)
    // layer1 → wiggle frame A
    // layer2 → wiggle frame B
    // layer3 → cursor / input
    // ============================================================

    public final Canvas layer0 = new Canvas(internalSize, internalSize);
    public final Canvas layer1 = new Canvas(internalSize, internalSize);
    public final Canvas layer2 = new Canvas(internalSize, internalSize);
    public final Canvas layer3 = new Canvas(internalSize, internalSize);

    /** StackPane that holds the 4 canvas layers aligned on top of each other. */
    private final StackPane layerPane = new StackPane();


    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Constructs the multi-layer canvas and prepares scaling,
     * anti-smoothing settings, theme bindings, and initial background.
     */
    public CanvasPane() {

        // Keep pixel-art sharp
        layer0.getGraphicsContext2D().setImageSmoothing(false);
        layer1.getGraphicsContext2D().setImageSmoothing(false);
        layer2.getGraphicsContext2D().setImageSmoothing(false);
        layer3.getGraphicsContext2D().setImageSmoothing(false);

        // Stack layers bottom → top
        layerPane.getChildren().addAll(layer0, layer1, layer2, layer3);

        // Scale up pixel buffer → display size
        double scale = displaySize / (double) internalSize;
        layerPane.setScaleX(scale);
        layerPane.setScaleY(scale);
        layerPane.setPrefSize(displaySize, displaySize);

        // Add to main Pane
        setPrefSize(displaySize, displaySize);
        getChildren().add(layerPane);

        // Draw theme background
        redrawBackground();

        // Update background when theme changes
        ThemeManager.addListener(this::redrawBackground);
    }


    // ============================================================
    // Theme background redraw
    // ============================================================

    /**
     * Redraws the background layer (layer0) using the current theme:
     * <ul>
     *     <li>fills background with theme.bg</li>
     *     <li>draws a 1px border in theme.fg</li>
     * </ul>
     */
    private void redrawBackground() {
        var gc = layer0.getGraphicsContext2D();

        gc.setFill(ThemeManager.get().bg);
        gc.fillRect(0, 0, internalSize, internalSize);

        gc.setStroke(ThemeManager.get().fg);
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, internalSize, internalSize);
    }


    // ============================================================
    // Coordinate conversion (display → internal)
    // ============================================================

    /**
     * Converts an X coordinate from display space (scaled) into internal
     * pixel-art space (200×200).
     *
     * @param x display pixel X
     * @return internal pixel-art X
     */
    public double toInternalX(double x) {
        return x * internalSize / displaySize;
    }

    /**
     * Converts a Y coordinate from display space into internal space.
     *
     * @param y display pixel Y
     * @return internal pixel-art Y
     */
    public double toInternalY(double y) {
        return y * internalSize / displaySize;
    }


    // ============================================================
    // Accessors
    // ============================================================

    /** @return internal pixel resolution (200). */
    public int getInternalSize() { return internalSize; }

    /** @return displayed resolution (500). */
    public int getDisplaySize() { return displaySize; }

    /** Alias for internal size, used by controllers. */
    public int getCanvasSize() { return internalSize; }

    /**
     * @return the cursor/input layer (layer3).
     * This is where eraser previews and input shapes are drawn.
     */
    public Canvas getCursorLayer() {
        return layer3;
    }


    // ============================================================
    // Cursor helper
    // ============================================================

    /**
     * Clears the cursor layer (layer3), removing eraser preview outlines.
     */
    public void clearCursor() {
        layer3.getGraphicsContext2D().clearRect(0, 0, internalSize, internalSize);
    }
}
