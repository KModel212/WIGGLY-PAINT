package brush;

import canvas.CanvasData;

public class MarkerPen extends AbstractBrush {
    public MarkerPen(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }

    @Override
    protected void stamp(CanvasData canvas, double x, double y, int size, int colorIndex, int frame) {

    }
}
