package brush;

import canvas.CanvasData;
import java.util.Random;

/**
 * Base class for spray-style brushes.
 * <p>
 * Extends {@link AbstractBrush} and adds a reusable
 * {@link #sprayCircle(CanvasData, double, double, int, int, int, int)}
 * helper for generating randomized spray patterns.
 * <p>
 * Subclasses only need to define how a “stamp” triggers this spray
 * method with custom density, radius, or behavior.
 */
public abstract class AbstractSpray extends AbstractBrush {

    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Creates a spray brush with a base size and speed scaling.
     *
     * @param baseSize   initial spray radius before scaling
     * @param speedScale factor controlling reduction when moving fast
     */
    public AbstractSpray(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }


    // ============================================================
    // Spray Algorithm
    // ------------------------------------------------------------
    // Subclasses call this method to spray random particles inside
    // a circular region. Each particle is a single pixel dot.
    //
    // "particles" determines density / thickness.
    // ============================================================

    /**
     * Sprays a number of random particles inside a circle with radius {@code radius}.
     * <p>
     * Algorithm:
     * <ul>
     *     <li>Picks a random angle from 0 to 2π</li>
     *     <li>Picks a distance using √random for uniform area distribution</li>
     *     <li>Converts polar → Cartesian to find pixel coordinate</li>
     *     <li>Writes that pixel into the canvas</li>
     * </ul>
     *
     * @param canvas     target canvas
     * @param x          center X coordinate
     * @param y          center Y coordinate
     * @param radius     spray radius
     * @param colorIndex color palette index
     * @param layer      canvas layer (used for wiggle)
     * @param particles  number of spray dots (density)
     */
    protected void sprayCircle(
            CanvasData canvas,
            double x, double y,
            int radius,
            int colorIndex,
            int layer,
            int particles
    ) {
        for (int i = 0; i < particles; i++) {

            // Random angle between 0 and 2π
            double angle = random.nextDouble() * Math.PI * 2;

            // Use sqrt to evenly distribute dots by area (not too clustered)
            double dist = radius * Math.sqrt(random.nextDouble());

            int px = (int) (x + Math.cos(angle) * dist);
            int py = (int) (y + Math.sin(angle) * dist);

            canvas.set(layer, px, py, (byte) colorIndex);
        }
    }
}
