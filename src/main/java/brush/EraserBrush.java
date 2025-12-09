package brush;

import canvas.CanvasData;

public class EraserBrush extends AbstractBrush {

    // ============================================================
    // Constructor
    // ------------------------------------------------------------
    // Eraser always uses BG color and ignores speed.
    // ============================================================
    public EraserBrush(int baseSize) {
        super(baseSize, CanvasData.BG, 0);
    }


    // ============================================================
    // Stamp — erase using a circular mask
    // ============================================================
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

                // inside circle
                if (dx * dx + dy * dy <= size * size) {
                    int px = cx + dx;
                    int py = cy + dy;

                    canvas.set(layer, px, py, CanvasData.BG);  // erase to background
                }
            }
        }
    }


    // ============================================================
    // Reset Stroke
    // ============================================================
    @Override
    public void resetStroke() {
        super.resetStroke(); // keeps continuous-line behavior consistent
    }
}
