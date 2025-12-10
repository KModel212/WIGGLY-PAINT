package brush;

import canvas.CanvasData;
import java.util.Random;

/**
 * A soft, jittery highlighting brush.
 * <p>
 * This brush simulates a hand-drawn highlighter stroke:
 * <ul>
 *     <li>Uses slight jitter to avoid perfectly smooth edges</li>
 *     <li>Supports different highlight colors (A/B/C variations)</li>
 *     <li>Uses the inherited {@link AbstractBrush#shape(int, int, int)}
 *         algorithm to create organic dab shapes</li>
 *     <li>Strokes across all wiggle layers for animation effect</li>
 * </ul>
 */
public class HighlightBrush extends AbstractBrush {

    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Creates a highlight brush.
     *
     * @param baseSize     highlight thickness
     * @param colorIndex   palette color index (e.g., highlight A/B/C)
     * @param speedScale   scaling factor when moving fast
     */
    public HighlightBrush(int baseSize, int colorIndex, double speedScale) {
        super(baseSize, colorIndex, speedScale);
    }


    // ============================================================
    // Stamp — soft, jittery highlight dab
    // ============================================================

    /**
     * Paints a jittered highlight dab at position (x, y).
     * <p>
     * Algorithm:
     * <ul>
     *     <li>Adds subtle jitter to make the highlight look organic</li>
     *     <li>Uses randomized shape sampling via {@code shape()}</li>
     *     <li>Writes pixels into the provided canvas layer</li>
     * </ul>
     *
     * @param canvas     canvas to draw on
     * @param x          logical x coordinate
     * @param y          logical y coordinate
     * @param size       calculated radius for this dab
     * @param colorIndex highlight color index
     * @param layer      canvas layer (wiggle A/B)
     */
    @Override
    protected void stamp(
            CanvasData canvas,
            double x, double y,
            int size,
            int colorIndex,
            int layer
    ) {
        int cx = (int) x;
        int cy = (int) y;

        // subtle jitter for the “hand-drawn” effect
        cx += (random.nextDouble() - 0.5) * 1.0;
        cy += (random.nextDouble() - 0.5) * 1.0;

        int r = size;

        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {

                if (shape(dx, dy, r)) {
                    int px = cx + dx;
                    int py = cy + dy;

                    canvas.set(layer, px, py, (byte) colorIndex);
                }
            }
        }
    }


    // ============================================================
    // Continuous stroke
    // ============================================================

    /**
     * Highlight strokes are continuous and smooth.
     * Delegates interpolation and layering logic to
     * {@link AbstractBrush#paintOnEveryLayer(CanvasData, double, double, double)}.
     *
     * @param canvas canvas to modify
     * @param x      current x position
     * @param y      current y position
     * @param speed  movement speed for size scaling
     */
    @Override
    public void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed) {
        super.paintOnEveryLayer(canvas, x, y, speed);
    }
}
