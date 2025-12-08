package brush;

import canvas.CanvasData;
import utils.themes.ThemeManager;

public abstract class AbstractBrush implements Brushable {

    protected int baseSize;       // minimum brush size
    protected double speedScale;  // how much speed affects size
    protected int frame;          // for wobble animation (0 or 1)
    protected int colorIndex;

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

    /**
     * The brush system calls this every time the pointer moves.
     */
    @Override
    public void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed) {
        int size = computeSize(speed);

        stamp(canvas, x, y, size, colorIndex, 1);
        stamp(canvas, x, y, size, colorIndex, 2);
    }

    protected abstract void stamp(CanvasData canvas, double x, double y, int size, int colorIndex, int frame);


    protected int computeSize(double speed) {
        double s = baseSize + speed * speedScale;
        return Math.max(2, (int) Math.round(s));
    }

    protected int getColorIndex() {
        return CanvasData.FG; // always use FG for brushes
    }

    public void setFrame(int f) {
        this.frame = f;
    }
}
