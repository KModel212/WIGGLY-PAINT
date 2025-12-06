package brush;

import canvas.CanvasData;
import java.util.Random;

public class PencilBrush extends AbstractBrush {

    private int lastDrawX = -1;
    private int lastDrawY = -1;
    private final Random rand = new Random();

    public PencilBrush() {
        super(1, 0);
    }

    @Override
    protected void stamp(CanvasData canvas, double x, double y,
                         int size, int color, int frame) {

        // --- Slight pixel-art snap (retro quantization) ---
        int x1 = (int) Math.round(x * 0.5) * 2;  // snap to even pixels
        int y1 = (int) Math.round(y * 0.5) * 2;

        // --- First pixel ---
        if (lastDrawX == -1) {
            canvas.setPixel(x1, y1, color);
            lastDrawX = x1;
            lastDrawY = y1;
            return;
        }

        int x0 = lastDrawX;
        int y0 = lastDrawY;

        // --- Bresenham line ---
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {

            // ------ 8-bit JITTER (retro noise) -------
            int jitterX = 0;
            int jitterY = 0;

            // add jitter every few pixels to simulate imperfect hardware
            if ((x0 + y0 + frame) % 6 == 0) {
                jitterX = rand.nextInt(3) - 1;  // -1,0,1
            }
            if ((x0 - y0 + frame) % 7 == 0) {
                jitterY = rand.nextInt(3) - 1;
            }

            canvas.setPixel(x0 + jitterX, y0 + jitterY, color);

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x0 += sx; }
            if (e2 < dx) { err += dx; y0 += sy; }
        }

        lastDrawX = x1;
        lastDrawY = y1;
    }

    public void reset() {
        lastDrawX = -1;
        lastDrawY = -1;
    }
}
