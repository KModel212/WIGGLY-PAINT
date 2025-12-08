package brush;

import canvas.CanvasData;

public class SprayBrush extends AbstractSpray {

    public SprayBrush(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }

    @Override
    protected void stamp(CanvasData canvas,
                         double x, double y,
                         int size, int colorIndex,
                         int layer) {

        int particles = size;  // density control
        sprayCircle(canvas, x, y, size, colorIndex, layer, particles);
    }
}
