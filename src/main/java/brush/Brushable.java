package brush;

import canvas.CanvasData;

public interface Brushable {

    /**
     * Called continuously while the mouse/finger is moving.
     *
     * @param canvas The pixel canvas buffer
     * @param x      current pointer X
     * @param y      current pointer Y
     * @param speed  motion speed since last event
     */
    void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed);

}
