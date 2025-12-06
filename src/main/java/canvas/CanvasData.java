package canvas;

import utils.themes.ThemeManager;

public class CanvasData {

    private final int width;
    private final int height;

    /**
     * Stores color indexes (0–4):
     * 0 = bg
     * 1 = fg
     * 2 = primary
     * 3 = secondary
     * 4 = accent
     */
    private final byte[][] pixels;

    public static final int BG = 0;
    public static final int FG = 1;
    public static final int PRIMARY = 2;
    public static final int SECONDARY = 3;
    public static final int ACCENT = 4;

    public CanvasData(int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new byte[width][height];

        // fill canvas with background index
        byte bgIndex = (byte) BG;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[x][y] = bgIndex;
            }
        }
    }

    // ----------------------------
    // PIXEL GET/SET
    // ----------------------------

    public void setPixel(int x, int y, int colorIndex) {
        if (x < 0 || x >= width) return;
        if (y < 0 || y >= height) return;

        pixels[x][y] = (byte) colorIndex;
    }

    public int getPixel(int x, int y) {
        if (x < 0 || x >= width) return BG;
        if (y < 0 || y >= height) return BG;

        return pixels[x][y] & 0xFF;
    }

    // ----------------------------
    // CLEAR
    // ----------------------------

    public void clear() {
        byte bg = (byte) BG;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[x][y] = bg;
            }
        }
    }

    // ----------------------------
    // COPY (Undo)
    // ----------------------------

    public CanvasData copy() {
        CanvasData copy = new CanvasData(width, height);
        for (int x = 0; x < width; x++) {
            System.arraycopy(pixels[x], 0, copy.pixels[x], 0, height);
        }
        return copy;
    }

    // ----------------------------
    // RAW ACCESSOR (Renderer)
    // ----------------------------

    public byte[][] raw() {
        return pixels;
    }

    // ----------------------------
    // INFO
    // ----------------------------

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
