package brush;

import canvas.CanvasData;

/**
 * The core interface for all brushes and painting tools in WigglyPaint.
 * <p>
 * Any class that implements {@code Paintable} can draw onto the canvas.
 * The system (CanvasController) calls these methods during pointer/mouse
 * movement to create strokes.
 */
public interface Paintable {

    // ============================================================
    // Paint event
    // ------------------------------------------------------------
    // Called continuously while pointer is moving.
    // Each brush implements how it applies paint to the canvas.
    // ============================================================

    /**
     * Called continuously during a drag/move event.
     * <p>
     * Brushes must:
     * <ul>
     *     <li>Compute size based on speed</li>
     *     <li>Stamp/interpolate between points</li>
     *     <li>Draw on all layers (usually 2 for wiggle)</li>
     * </ul>
     *
     * @param canvas the target canvas containing all paint layers
     * @param x      current pointer x-coordinate
     * @param y      current pointer y-coordinate
     * @param speed  pointer movement speed (used for dynamic sizing)
     */
    void paintOnEveryLayer(CanvasData canvas, double x, double y, double speed);


    // ============================================================
    // Reset continuous stroke state (for new line)
    // ============================================================

    /**
     * Resets internal stroke history.
     * <p>
     * Called when the user ends a stroke or begins a new stroke.
     * Most brushes clear their last position so line interpolation
     * starts fresh.
     */
    void resetStroke();
}
