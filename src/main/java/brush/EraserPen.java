package brush;

import canvas.CanvasData;

public class EraserPen extends AbstractBrush {
    public EraserPen(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }

    @Override
    protected void stamp(CanvasData canvas, double x, double y, int size, int colorIndex, int frame) {

    }
}
