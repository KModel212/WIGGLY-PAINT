package brush;

import canvas.CanvasData;
import java.util.Random;

public class PencilBrush extends AbstractBrush {

    private final Random random = new Random();

    public PencilBrush(int baseSize) {
        super(baseSize);
    }

    @Override
    protected void stamp(CanvasData canvas, double x, double y, int size, int colorIndex, int layer) {

        int cx = (int) x;
        int cy = (int) y;

        // random jitter per layer
        double jitterX = (random.nextDouble() - 0.5) * 1.0;
        double jitterY = (random.nextDouble() - 0.5) * 1.0;

        cx += jitterX;
        cy += jitterY;

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

    /**
     * Returns true if pixel (dx, dy) should be drawn.
     * Uses multiple randomly chosen circle-like shapes.
     */
    private boolean shape(int dx, int dy, int r) {
        int rdm = random.nextInt(4);
        //System.out.print(rdm);
        switch (rdm) {

            case 0: // Perfect circle
                return dx * dx + dy * dy <= r * r;

            case 1: { // Wobbly circle
                double jitter = (random.nextDouble() - 0.5) * 0.6;
                double rr = (r + jitter) * (r + jitter);
                return dx * dx + dy * dy <= rr;
            }

            case 2: // Dotted circle (halftone style)
                return ((dx + dy) & 1) == 0 && dx * dx + dy * dy <= r * r;

            case 3: { // Ellipse (calligraphy effect)
                double a = r * 1.3;
                double b = r * 0.8;
                return (dx * dx) / (a * a) + (dy * dy) / (b * b) <= 1.0;
            }
        }

        return false;
    }

    @Override
    public void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed) {
        // Always draw for marker — no broken effect
        super.paintOnEveryLayer(canvas, x, y, speed);
    }
}
