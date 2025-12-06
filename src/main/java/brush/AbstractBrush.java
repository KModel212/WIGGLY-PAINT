package brush;

import canvas.CanvasData;
import utils.themes.ThemeManager;

public abstract class AbstractBrush implements Brushable {

    protected int baseSize;       // minimum brush size
    protected double speedScale;  // how much speed affects size
    protected int frame;          // for wobble animation (0 or 1)

    public AbstractBrush(int baseSize, double speedScale) {
        this.baseSize = baseSize;
        this.speedScale = speedScale;
    }

    /**
     * The brush system calls this every time the pointer moves.
     */
    @Override
    public void paint(CanvasData canvas, double x, double y, double speed) {
        int size = computeSize(speed);
        int colorIndex = getColorIndex();

        stamp(canvas, x, y, size, colorIndex, frame);
    }

    /**
     * Override this to implement the actual stamping behavior.
     * This is where PencilBrush, FountainBrush, etc. draw pixels.
     */
    protected abstract void stamp(CanvasData canvas, double x, double y,
                                  int size, int colorIndex, int frame);

    /**
     * Computes brush size based on speed.
     * Faster movement → bigger stroke.
     */
    protected int computeSize(double speed) {
        double s = baseSize + speed * speedScale;
        return Math.max(1, (int) Math.round(s));
    }

    /**
     * Brushes use the current FG color.
     */
    protected int getColorIndex() {
        return CanvasData.FG; // always use FG for brushes
    }

    /**
     * For animation: frame = 0 or 1 (wiggly mode)
     */
    public void setFrame(int f) {
        this.frame = f;
    }
}
