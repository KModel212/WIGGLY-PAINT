package brush;

import canvas.CanvasData;
import java.util.Random;

/**
 * A brush that simulates a fountain pen stroke.
 * <p>
 * Behavior characteristics:
 * <ul>
 *     <li>Applies slight jitter to each dab, giving a hand-drawn wobble</li>
 *     <li>Uses the shared {@link AbstractBrush#shape(int, int, int)} for
 *         organic circle variation</li>
 *     <li>Supports speed-based size scaling (thin when drawing fast)</li>
 *     <li>Always draws continuous lines across wiggle layers</li>
 * </ul>
 */
public class FountainBrush extends AbstractBrush {

    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Creates a fountain pen–style brush with adjustable size and
     * speed-based scaling.
     *
     * @param baseSize   stroke size when moving slowly
     * @param speedScale reduction strength when moving quickly
     */
    public FountainBrush(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }


    // ============================================================
    // Stamp — fountain-like jittery circle
    // ============================================================

    /**
     * Places a jittered circular dab on the canvas.
     * <p>
     * Algorithm:
     * <ul>
     *     <li>Adds a small random offset to (x, y) to simulate fountain wobble</li>
     *     <li>Draws a randomized circular/elliptic shape (via {@code shape()})</li>
     *     <li>Writes each pixel into the selected canvas layer</li>
     * </ul>
     *
     * @param canvas     target canvas
     * @param x          original X position
     * @param y          original Y position
     * @param size       computed brush radius
     * @param colorIndex color index to write
     * @param layer      canvas layer (for wiggle)
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

        // jitter gives the fountain-pen wobble effect
        double jitterX = (random.nextDouble() - 0.5) * 1.0;
        double jitterY = (random.nextDouble() - 0.5) * 1.0;

        cx += (int) jitterX;
        cy += (int) jitterY;

        int radius = size;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {

                if (shape(dx, dy, radius)) {
                    int px = cx + dx;
                    int py = cy + dy;

                    canvas.set(layer, px, py, (byte) colorIndex);
                }
            }
        }
    }


    // ============================================================
    // Continuous stroke behavior
    // ============================================================

    /**
     * Fountain brush draws smooth, continuous strokes across layers.
     * Delegates interpolation to {@link AbstractBrush#paintOnEveryLayer}.
     *
     * @param canvas the canvas to draw on
     * @param x      current x position
     * @param y      current y position
     * @param speed  movement speed for size scaling
     */
    @Override
    public void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed) {
        super.paintOnEveryLayer(canvas, x, y, speed);
    }
}
