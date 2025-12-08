package brush;

import canvas.CanvasData;
import java.util.Random;

public class FountainBrush extends AbstractBrush {

    private final Random random = new Random();

    public FountainBrush(int baseSize , double speedScale) {
        super(baseSize , speedScale);
    }

    @Override
    protected void stamp(CanvasData canvas, double x, double y, int size, int colorIndex, int layer) {

        int cx = (int) x;
        int cy = (int) y;

        // random jitter per layer
        double jitterX = (random.nextDouble() - 0.5) * 1.0;
        double jitterY = (random.nextDouble() - 0.5) * 1.0;

        cx += (int) jitterX;
        cy += (int) jitterY;

        int radius = size;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {

                int px = cx + dx;
                int py = cy + dy;

                if (shape(dx, dy, radius)) {
                    canvas.set(layer, px, py, (byte) colorIndex);
                }
            }
        }
    }

    @Override
    public void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed) {
        // Always draw for marker — no broken effect
        super.paintOnEveryLayer(canvas, x, y, speed);
    }
}
