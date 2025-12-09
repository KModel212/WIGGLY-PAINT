package brush;

import canvas.CanvasData;
import java.util.Random;

public abstract class AbstractSpray extends AbstractBrush {

    // ============================================================
    // Fields
    // ============================================================
    protected final Random random = new Random();


    // ============================================================
    // Constructor
    // ============================================================
    public AbstractSpray(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }


    // ============================================================
    // Spray Algorithm
    // ------------------------------------------------------------
    // Subclasses call this to spray random particles within a circle.
    // "particles" = density/thickness of spray
    // ============================================================
    protected void sprayCircle(
            CanvasData canvas,
            double x, double y,
            int radius,
            int colorIndex,
            int layer,
            int particles
    ) {
        for (int i = 0; i < particles; i++) {

            // random angle 0–2π
            double angle = random.nextDouble() * Math.PI * 2;

            // sqrt for uniform distribution inside circle
            double dist = radius * Math.sqrt(random.nextDouble());

            int px = (int) (x + Math.cos(angle) * dist);
            int py = (int) (y + Math.sin(angle) * dist);

            canvas.set(layer, px, py, (byte) colorIndex);
        }
    }
}
