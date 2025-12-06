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
    void paint(CanvasData canvas, double x, double y, double speed);

    /**
     * Optional: for wiggly 2-frame brushes (frame = 0 or 1)
     * Pens can ignore this and do nothing.
     */
    default void setFrame(int frame) { }
}
