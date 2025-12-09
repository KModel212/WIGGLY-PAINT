package brush;

import canvas.CanvasData;
import java.util.Random;

public class HighlightBrush extends AbstractBrush {

    // ============================================================
    // Constructor
    // ============================================================
    public HighlightBrush(int baseSize, int colorIndex, double speedScale) {
        super(baseSize, colorIndex, speedScale);
    }


    // ============================================================
    // Stamp — soft, jittery highlight dab
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

        // subtle jitter for hand-drawn highlight look
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
    @Override
    public void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed) {
        super.paintOnEveryLayer(canvas, x, y, speed);
    }
}
