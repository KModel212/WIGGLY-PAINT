package brush;

import canvas.CanvasData;
import java.util.Random;

public abstract class AbstractSpray extends AbstractBrush {

    protected final Random random = new Random();

    public AbstractSpray(int baseSize, double speedScale) {
        super(baseSize,  speedScale);
    }

    /**
     * Spray algorithm: randomly scatter points inside a circle.
     * Subclass defines how many particles per stamp.
     */
    protected void sprayCircle(CanvasData canvas,
                               double x, double y,
                               int radius, int colorIndex,
                               int layer,
                               int particles) {

        for (int i = 0; i < particles; i++) {

            // random angle + random distance (uniform inside circle)
            double angle = random.nextDouble() * Math.PI * 2;
            double dist = radius * Math.sqrt(random.nextDouble());

            int px = (int) (x + Math.cos(angle) * dist);
            int py = (int) (y + Math.sin(angle) * dist);

            canvas.set(layer, px, py, (byte) colorIndex);
        }
    }
}
