package brush;

import canvas.CanvasData;
import java.util.Random;

public class PencilBrush extends AbstractBrush {

    // ============================================================
    // Constructor
    // ============================================================
    public PencilBrush(int baseSize) {
        super(baseSize);
    }


    // ============================================================
    // Stamp — jittery pencil-like circle
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

        // subtle jitter → handmade pencil effect
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
    @Override
    public void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed) {
        super.paintOnEveryLayer(canvas, x, y, speed);
    }
}
