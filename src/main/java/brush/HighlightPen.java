package brush;

import canvas.CanvasData;

public class HighlightPen extends AbstractBrush {
    public HighlightPen(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }

    @Override
    protected void stamp(CanvasData canvas, double x, double y, int size, int colorIndex, int frame) {

    }
}
