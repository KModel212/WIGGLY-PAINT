package brush;

import canvas.CanvasData;
import java.util.Random;

public class FountainBrush extends AbstractBrush {

    // ============================================================
    // Constructor
    // ============================================================
    public FountainBrush(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }


    // ============================================================
    // Stamp — fountain-like jittery circle
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

        // jitter gives the "fountain pen" wobble effect
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
    @Override
    public void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed) {
        // Fountain always draws continuous lines
        super.paintOnEveryLayer(canvas, x, y, speed);
    }
}
