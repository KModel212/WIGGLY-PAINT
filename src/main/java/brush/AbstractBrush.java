package brush;

import canvas.CanvasData;
import utils.themes.ThemeManager;

import java.util.Random;

/**
 * Base class for all brushes used in WigglyPaint.
 * <p>
 * Handles shared behaviors such as:
 * <ul>
 *     <li>Size computation based on movement speed</li>
 *     <li>Interpolated stamping along continuous strokes</li>
 *     <li>Random shape variation for organic brush textures</li>
 *     <li>Painting on multiple layers (for wiggle effect)</li>
 * </ul>
 * Subclasses must implement {@link #stamp(CanvasData, double, double, int, int, int)}
 * to define their own dab shape.
 */
public abstract class AbstractBrush implements Paintable {

    // ============================================================
    // Fields
    // ============================================================

    /** Base brush size before speed scaling is applied. */
    protected int baseSize;

    /**
     * Strength of size reduction as speed increases.
     * Higher values → thinner stroke while moving fast.
     */
    protected double speedScale;

    /** Index into CanvasData color palette. */
    protected int colorIndex;

    /** Last stroke position, used for line interpolation. */
    protected double lastX = Double.NaN;
    protected double lastY = Double.NaN;

    /** Shared random generator for shape jitter / variations. */
    protected final Random random = new Random();


    // ============================================================
    // Constructors
    // ============================================================

    /**
     * Constructs a brush with default color and no speed scaling.
     *
     * @param baseSize base stroke size
     */
    public AbstractBrush(int baseSize) {
        this.baseSize = baseSize;
        this.speedScale = 0;
        this.colorIndex = CanvasData.FG;
    }

    /**
     * Constructs a brush with base size and speed scaling.
     *
     * @param baseSize   stroke size at zero speed
     * @param speedScale size reduction factor when moving fast
     */
    public AbstractBrush(int baseSize, double speedScale) {
        this.baseSize = baseSize;
        this.speedScale = speedScale;
        this.colorIndex = CanvasData.FG;
    }

    /**
     * Constructs a brush with full control over color and scaling.
     *
     * @param baseSize   initial size
     * @param colorIndex color index in CanvasData palette
     * @param speedScale speed-based size reduction factor
     */
    public AbstractBrush(int baseSize, int colorIndex, double speedScale) {
        this.baseSize = baseSize;
        this.speedScale = speedScale;
        this.colorIndex = colorIndex;
    }


    // ============================================================
    // Abstract — implemented by each brush
    // ============================================================

    /**
     * Places a single “dab” of the brush shape onto the canvas.
     * Subclasses define the exact pixel pattern.
     *
     * @param canvas     target canvas
     * @param x          x position
     * @param y          y position
     * @param size       computed brush size
     * @param colorIndex color to apply
     * @param layer      canvas layer index (wiggle uses multiple)
     */
    protected abstract void stamp(
            CanvasData canvas,
            double x, double y,
            int size,
            int colorIndex,
            int layer
    );


    // ============================================================
    // LINE STAMPING — interpolated stamps between two points
    // ============================================================

    /**
     * Draws a smooth line between the previous point (x0, y0)
     * and the new point (x1, y1) by placing interpolated dabs.
     *
     * @param canvas     the canvas to write to
     * @param x0         previous position (may be NaN)
     * @param y0         previous position (may be NaN)
     * @param x1         new x position
     * @param y1         new y position
     * @param size       computed brush size
     * @param colorIndex color index
     * @param layer      canvas layer index
     */
    protected void stampLine(
            CanvasData canvas,
            double x0, double y0,
            double x1, double y1,
            int size, int colorIndex,
            int layer
    ) {
        // First point in stroke → draw single stamp
        if (Double.isNaN(x0) || Double.isNaN(y0)) {
            stamp(canvas, x1, y1, size, colorIndex, layer);
            return;
        }

        double dx = x1 - x0;
        double dy = y1 - y0;
        double dist = Math.sqrt(dx * dx + dy * dy);

        // Higher density = smoother line
        int steps = Math.max(1, (int) (dist * 1));
        double sx = dx / steps;
        double sy = dy / steps;

        double px = x0;
        double py = y0;

        for (int i = 0; i < steps; i++) {
            stamp(canvas, px, py, size, colorIndex, layer);
            px += sx;
            py += sy;
        }
    }


    // ============================================================
    // SHAPE LOGIC — chooses a random brush dab shape
    // ============================================================

    /**
     * Randomly selects one of several shape modes:
     * <ul>
     *     <li>Perfect circle</li>
     *     <li>Wobbly jittered circle</li>
     *     <li>Half-tone circle (checker pattern)</li>
     *     <li>Ellipse (calligraphy effect)</li>
     * </ul>
     * This gives brushes a natural, varied texture.
     *
     * @param dx horizontal offset from center
     * @param dy vertical offset from center
     * @param r  radius
     * @return true if the pixel should be drawn
     */
    protected boolean shape(int dx, int dy, int r) {
        Random random = new Random();
        int rdm = random.nextInt(4);

        switch (rdm) {
            case 0: // Perfect circle
                return dx * dx + dy * dy <= r * r;

            case 1: { // Wobbly circle
                double jitter = (random.nextDouble() - 0.5) * 0.6;
                double rr = (r + jitter) * (r + jitter);
                return dx * dx + dy * dy <= rr;
            }

            case 2: // Halftone circle
                return ((dx + dy) & 1) == 0 && dx * dx + dy * dy <= r * r;

            case 3: { // Ellipse shape
                double a = r * 1.3;
                double b = r * 0.8;
                return (dx * dx) / (a * a) + (dy * dy) / (b * b) <= 1.0;
            }
        }
        return false;
    }


    // ============================================================
    // Size computation — reduce size when moving fast
    // ============================================================

    /**
     * Computes the brush size based on movement speed.
     * <p>
     * Uses a shallow log curve to avoid aggressive shrinking.
     *
     * @param speed recent movement speed from CanvasController
     * @return final pixel radius (min = 1)
     */
    protected int computeSize(double speed) {
        double shallow = Math.log1p(speed * 0.5); // slow, smooth curve
        double scaled = baseSize - speedScale * shallow;
        return Math.max(1, (int) Math.round(scaled));
    }


    // ============================================================
    // Reset continuous stroke
    // ============================================================

    /**
     * Resets the previous stroke point so the next drag event
     * starts a new line.
     */
    public void resetStroke() {
        lastX = Double.NaN;
        lastY = Double.NaN;
    }


    // ============================================================
    // Draw to all canvas layers (wiggle A/B)
    // ============================================================

    /**
     * Paints the brush stroke on every wiggle layer.
     * <p>
     * WigglyPaint uses 2 layers:
     * <ul>
     *     <li>Layer 1: main stroke</li>
     *     <li>Layer 2: slightly offset wiggle clone</li>
     * </ul>
     *
     * @param canvas the canvas containing pixel layers
     * @param x      current x
     * @param y      current y
     * @param speed  movement speed for size scaling
     */
    @Override
    public void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed) {
        int size = computeSize(speed);
        int totalLayers = 2;

        for (int layer = 1; layer <= totalLayers; layer++) {
            stampLine(canvas, lastX, lastY, x, y, size, colorIndex, layer);
        }

        lastX = x;
        lastY = y;
    }


    // ============================================================
    // Getters
    // ============================================================

    /**
     * @return the default brush size (before speed scaling)
     */
    public int getBaseSize() {
        return baseSize;
    }

    /**
     * @return the palette index of the active brush color
     */
    public int getColorIndex() {
        return colorIndex;
    }
}
