package brush;

import canvas.CanvasData;

/**
 * A simple spray-paint style brush.
 * <p>
 * Uses the {@link AbstractSpray#sprayCircle(CanvasData, double, double, int, int, int, int)}
 * helper to scatter random particles inside a circular radius.
 * Produces a soft, airbrush-like texture.
 */
public class SprayBrush extends AbstractSpray {

    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Creates a spray brush with a given base size and speed scaling.
     *
     * @param baseSize   spray radius before scaling
     * @param speedScale controls how spray radius changes with speed
     */
    public SprayBrush(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }


    // ============================================================
    // Stamp — spray particles using AbstractSpray helper
    // ============================================================

    /**
     * Applies a spray dab at the given (x, y) position by scattering
     * random particles inside a circle.
     * <p>
     * Density rule:
     * <ul>
     *     <li>Number of particles = brush size</li>
     *     <li>Larger brush → denser spray</li>
     * </ul>
     *
     * @param canvas     target canvas
     * @param x          spray center X
     * @param y          spray center Y
     * @param size       computed spray radius
     * @param colorIndex palette color index
     * @param layer      canvas layer (for wiggle)
     */
    @Override
    protected void stamp(
            CanvasData canvas,
            double x, double y,
            int size,
            int colorIndex,
            int layer
    ) {
        int particles = size;  // simple density → proportional to radius
        sprayCircle(canvas, x, y, size, colorIndex, layer, particles);
    }
}
