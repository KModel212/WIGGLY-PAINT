package brush;

import canvas.CanvasData;

public class SprayBrush extends AbstractSpray {

    // ============================================================
    // Constructor
    // ============================================================
    public SprayBrush(int baseSize, double speedScale) {
        super(baseSize, speedScale);
    }


    // ============================================================
    // Stamp — spray particles using AbstractSpray helper
    // ============================================================
    @Override
    protected void stamp(
            CanvasData canvas,
            double x, double y,
            int size,
            int colorIndex,
            int layer
    ) {
        int particles = size;   // density control (simple)
        sprayCircle(canvas, x, y, size, colorIndex, layer, particles);
    }
}
