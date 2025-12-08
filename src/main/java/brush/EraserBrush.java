package brush;

import canvas.CanvasData;

public class EraserBrush extends AbstractBrush {

    public EraserBrush(int baseSize) {
        // colorIndex = BG, speedScale = 0 (eraser size does not depend on speed)
        super(baseSize, CanvasData.BG, 0);
    }

    @Override
    protected void stamp(CanvasData canvas,
                         double x, double y,
                         int size,
                         int colorIndex,
                         int layer) {

        int cx = (int) x;
        int cy = (int) y;

        // simple erase circle
        for (int dx = -size; dx <= size; dx++) {
            for (int dy = -size; dy <= size; dy++) {

                if (dx * dx + dy * dy <= size * size) {
                    int px = cx + dx;
                    int py = cy + dy;

                    canvas.set(layer, px, py, CanvasData.BG);   // erase to background
                }
            }
        }
    }

    @Override
    public void resetStroke() {
        super.resetStroke();  // keep the same continuous-line behavior
    }
}
