package brush;

import canvas.CanvasData;
import utils.themes.ThemeManager;

import java.util.Random;

public abstract class AbstractBrush implements Paintable {

    // ============================================================
    // Fields
    // ============================================================
    protected int baseSize;
    protected double speedScale;
    protected int frame;
    protected int colorIndex;

    // last stroke point (used for stampLine)
    protected double lastX = Double.NaN;
    protected double lastY = Double.NaN;

    // random for random the circle shape
    protected final Random random = new Random();

    // ============================================================
    // Constructors
    // ============================================================
    public AbstractBrush(int baseSize) {
        this.baseSize = baseSize;
        this.speedScale = 0;
        this.colorIndex = CanvasData.FG;
    }

    public AbstractBrush(int baseSize, double speedScale) {
        this.baseSize = baseSize;
        this.speedScale = speedScale;
        this.colorIndex = CanvasData.FG;
    }

    public AbstractBrush(int baseSize, int colorIndex, double speedScale) {
        this.baseSize = baseSize;
        this.speedScale = speedScale;
        this.colorIndex = colorIndex;
    }


    // ============================================================
    // Abstract — implemented by each brush
    // ============================================================
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
    protected void stampLine(
            CanvasData canvas,
            double x0, double y0,
            double x1, double y1,
            int size, int colorIndex,
            int layer
    ) {

        // First point: draw only one
        if (Double.isNaN(x0) || Double.isNaN(y0)) {
            stamp(canvas, x1, y1, size, colorIndex, layer);
            return;
        }

        double dx = x1 - x0;
        double dy = y1 - y0;
        double dist = Math.sqrt(dx * dx + dy * dy);

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
    protected boolean shape(int dx, int dy, int r) {

        Random random = new Random();
        int rdm = random.nextInt(4);

        switch (rdm) {

            case 0: // perfect circle
                return dx * dx + dy * dy <= r * r;

            case 1: { // wobbly circle
                double jitter = (random.nextDouble() - 0.5) * 0.6;
                double rr = (r + jitter) * (r + jitter);
                return dx * dx + dy * dy <= rr;
            }

            case 2: // halftone circle
                return ((dx + dy) & 1) == 0 && dx * dx + dy * dy <= r * r;

            case 3: { // ellipse (calligraphy effect)
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
    protected int computeSize(double speed) {
        double shallow = Math.log1p(speed * 0.5); // slow growth
        double scaled = baseSize - speedScale * shallow;
        return Math.max(1, (int) Math.round(scaled));
    }


    // ============================================================
    // Reset continuous stroke
    // ============================================================
    public void resetStroke() {
        lastX = Double.NaN;
        lastY = Double.NaN;
    }


    // ============================================================
    // Draw to all canvas layers (wiggle A/B)
    // ============================================================
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
    public int getBaseSize() {
        return baseSize;
    }

    public int getColorIndex() {
        return colorIndex;
    }
}
