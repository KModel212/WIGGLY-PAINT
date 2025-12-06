package brush;

import canvas.CanvasData;
import utils.themes.ThemeManager;

public abstract class AbstractPen implements Brushable {

    protected int size;

    public AbstractPen(int size) {
        this.size = size;
    }

    @Override
    public void paint(CanvasData canvas, double x, double y, double speed) {
        int color = getColorIndex();
        stamp(canvas, x, y, size, color);
    }

    /** Subclasses implement the actual drawing */
    protected abstract void stamp(CanvasData canvas, double x, double y,
                                  int size, int color);

    /** Pens always use FG except HighlightPen */
    protected int getColorIndex() {
        return CanvasData.FG;
    }

    public void setSize(int s) {
        this.size = s;
    }
}
