package brush;

import canvas.CanvasData;
import java.util.Random;

/**
 * A simple pencil-style brush.
 * <p>
 * Produces a hand-drawn pencil texture by adding slight jitter to the
 * stamping location and relying on {@link AbstractBrush#shape(int, int, int)}
 * to generate organic circular shapes. Good for sketching and fine lines.
 */
public class PencilBrush extends AbstractBrush {

    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Creates a pencil brush with a given base size.
     * <p>
     * Speed scaling is inherited from {@link AbstractBrush} and defaults to 0,
     * meaning pencil size stays consistent regardless of movement speed.
     *
     * @param baseSize pencil stroke radius
     */
    public PencilBrush(int baseSize) {
        super(baseSize);
    }


    // ============================================================
    // Stamp — jittery pencil-like circle
    // ============================================================

    /**
     * Draws a jittered pencil dab at the given position.
     * <p>
     * Behavior:
     * <ul>
     *     <li>Introduces ±0.5px jitter for a hand-drawn look</li>
     *     <li>Uses {@code shape()} to produce circle/ellipse/halftone variants</li>
     *     <li>Writes each dab pixel into a specific canvas layer</li>
     * </ul>
     *
     * @param canvas     canvas to draw on
     * @param x          dab center X coordinate
     * @param y          dab center Y coordinate
     * @param size       computed radius based on brush settings
     * @param colorIndex color index in palette
     * @param layer      canvas layer index
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

        // subtle jitter → pencil texture
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
    // Continuous stroke behavior
    // ============================================================

    /**
     * Pencil brush uses the default continuous stroke behavior provided
     * by {@link AbstractBrush#paintOnEveryLayer(CanvasData, double, double, double)}.
     *
     * @param canvas canvas to modify
     * @param x      current pointer x position
     * @param y      current pointer y position
     * @param speed  movement speed (ignored unless speedScale > 0)
     */
    @Override
    public void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed) {
        super.paintOnEveryLayer(canvas, x, y, speed);
    }
}
