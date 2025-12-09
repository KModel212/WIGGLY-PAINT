package brush;

import canvas.CanvasData;

public interface Paintable {

    // ============================================================
    // Paint event
    // ------------------------------------------------------------
    // Called continuously while pointer is moving.
    // Each brush implements how it applies paint to the canvas.
    // ============================================================
    void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed);


    // ============================================================
    // Reset continuous stroke state (for new line)
    // ============================================================
    void resetStroke();
}
