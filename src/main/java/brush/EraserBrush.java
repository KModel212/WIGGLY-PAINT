package brush;

import canvas.CanvasData;

/**
 * A brush that erases pixels by writing the background color (BG)
 * into all canvas layers. Behaves like a circular soft eraser.
 * <p>
 * Unlike other brushes:
 * <ul>
 *     <li>Color is always {@link CanvasData#BG}</li>
 *     <li>Speed scaling is always disabled</li>
 *     <li>Stamp shape is a perfect filled circle</li>
 * </ul>
 */
public class EraserBrush extends AbstractBrush {

    // ============================================================
    // Constructor
    // ------------------------------------------------------------
    // Eraser always uses BG color and ignores speed.
    // ============================================================

    /**
     * Creates an eraser with the given base size.
     * <p>
     * Speed scaling is disabled, and color is locked to BG.
     *
     * @param baseSize radius of the eraser circle
     */
    public EraserBrush(int baseSize) {
        super(baseSize, CanvasData.BG, 0);
    }


    // ============================================================
    // Stamp — erase using a circular mask
    // ============================================================

    /**
     * Erases pixels inside a filled circle centered at (x, y).
     * <p>
     * For each pixel within the radius, the brush writes
     * {@link CanvasData#BG} to the specified layer.
     *
     * @param canvas     target canvas
     * @param x          center x
     * @param y          center y
     * @param size       eraser radius
     * @param colorIndex ignored (eraser always uses BG)
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

        for (int dx = -size; dx <= size; dx++) {
            for (int dy = -size; dy <= size; dy++) {

                // inside circle = erase
                if (dx * dx + dy * dy <= size * size) {
                    int px = cx + dx;
                    int py = cy + dy;

                    canvas.set(layer, px, py, CanvasData.BG);
                }
            }
        }
    }


    // ============================================================
    // Reset Stroke
    // ============================================================

    /**
     * Resets stroke state (used for interpolation).
     * The eraser keeps the same behavior as other brushes.
     */
    @Override
    public void resetStroke() {
        super.resetStroke();
    }
}
