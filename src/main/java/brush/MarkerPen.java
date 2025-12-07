package brush;

import canvas.CanvasData;

import java.util.Random;


public class MarkerPen extends AbstractBrush {

    public MarkerPen(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }

    @Override
    protected void stamp(CanvasData canvas,
                         double x, double y,
                         int size,
                         int colorIndex,
                         int frame) {

        int ix = (int) x;
        int iy = (int) y;

        Random random = new Random();
        // Random number of pixels in the stroke (1–4)
        int count = 1 + random.nextInt(4);

        // 8 possible directions
        int[][] dirs = {
                { 1, 0}, {-1, 0},  // horizontal
                { 0, 1}, { 0,-1},  // vertical
                { 1, 1}, {-1, 1},  // diagonals
                { 1,-1}, {-1,-1}
        };

        // Place the first pixel at the center
        canvas.set(1, ix, iy, (byte) colorIndex);

        int cx = ix;
        int cy = iy;

        for (int i = 1; i < count; i++) {
            // pick a random direction
            int[] d = dirs[random.nextInt(dirs.length)];

            // step outward
            cx += d[0];
            cy += d[1];

            // draw pixel
            canvas.set(2 , cx, cy, (byte) colorIndex);
        }
    }


    @Override
    public void paint(CanvasData canvas, double x, double y, double speed) {
        // 50% chance to skip so the stroke becomes broken
        Random random = new Random();
        if (random.nextDouble() > 0.8) {
            paint(canvas, x, y, speed);
            return;
        }


        super.paint(canvas, x, y, speed);
    }

}
