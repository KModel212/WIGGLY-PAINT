package canvas;

import gui.CanvasPane;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

import java.awt.image.BufferedImage;

/**
 * Stores all pixel data for WigglyPaint's canvas, including
 * the base framebuffer and two animated “wiggle” frames.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Maintain pixel buffers for base + wiggle layers</li>
 *     <li>Provide read/write access to layers</li>
 *     <li>Handle safe pixel-setting with bounds checks</li>
 *     <li>Convert any framebuffer to a {@link BufferedImage}
 *         for GIF export or screenshots</li>
 * </ul>
 *
 * Pixel values are stored as small byte indices that map to
 * theme-controlled {@link javafx.scene.paint.Color} values.
 */
public class CanvasData {

    // ============================================================
    // Color indices
    // ============================================================

    /** Background color index (theme-dependent). */
    public static final byte BG = 0;

    /** Foreground color index (main paint color). */
    public static final byte FG = 1;

    /** Primary accent color for brush variations. */
    public static final byte PRIMARY = 2;

    /** Secondary accent color. */
    public static final byte SECONDARY = 3;

    /** Highlight/accent color. */
    public static final byte ACCENT = 4;

    /** Logical pixel width/height of the canvas. */
    private static final int SIZE = 200;


    // ============================================================
    // Framebuffers
    // ------------------------------------------------------------
    // pixels       : base framebuffer
    // wiggleFrameA : wiggle animation frame #1
    // wiggleFrameB : wiggle animation frame #2
    // ============================================================

    private final byte[][] pixels      = new byte[SIZE][SIZE];
    public  final byte[][] wiggleFrameA = new byte[SIZE][SIZE];
    public  final byte[][] wiggleFrameB = new byte[SIZE][SIZE];


    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Constructs a new canvas and fills wiggle frames with BG color.
     */
    public CanvasData() {
        initWiggleFrames(BG);
    }


    // ============================================================
    // Initialization
    // ============================================================

    /**
     * Initializes both wiggle animation buffers to a given color.
     *
     * @param color the byte index to fill with
     */
    private void initWiggleFrames(byte color) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                wiggleFrameA[x][y] = color;
                wiggleFrameB[x][y] = color;
            }
        }
    }


    // ============================================================
    // Frame accessors
    // ============================================================

    /** @return the canvas width/height (square). */
    public int getSize() { return SIZE; }

    /** @return raw base framebuffer. */
    public byte[][] raw() { return pixels; }

    /** @return wiggle animation frame A. */
    public byte[][] A() { return wiggleFrameA; }

    /** @return wiggle animation frame B. */
    public byte[][] B() { return wiggleFrameB; }


    // ============================================================
    // Write a pixel into the specified layer
    // ------------------------------------------------------------
    // layeridx:
    //   0 → base pixels
    //   1 → wiggle frame A
    //   2 → wiggle frame B
    // ============================================================

    /**
     * Writes a pixel value into one of the canvas layers.
     * Safely ignores coordinates outside the canvas bounds.
     *
     * @param layeridx which framebuffer to modify (0, 1, or 2)
     * @param x        x coordinate
     * @param y        y coordinate
     * @param index    color index to write
     */
    public void set(int layeridx, int x, int y, byte index) {

        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return;

        switch (layeridx) {
            case 0 -> pixels[x][y]       = index;
            case 1 -> wiggleFrameA[x][y] = index;
            case 2 -> wiggleFrameB[x][y] = index;
            default -> { /* ignore invalid layers */ }
        }
    }


    // ============================================================
    // Clear all layers
    // ============================================================

    /**
     * Clears all canvas layers to a given color index.
     *
     * @param color the byte index to fill all pixels with
     */
    public void clearAll(byte color) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                pixels[x][y]       = color;
                wiggleFrameA[x][y] = color;
                wiggleFrameB[x][y] = color;
            }
        }
    }


    // ============================================================
    // Convert framebuffer → BufferedImage
    // Used for export GIF / screenshot
    // ============================================================

    /**
     * Converts a framebuffer into a {@link BufferedImage}.
     * <p>
     * Each framebuffer pixel stores a byte color index which maps to
     * theme colors from {@link ThemeManager}. The resulting image is
     * ARGB and ready for GIF export, saving, or preview.
     *
     * @param frame the framebuffer to convert (pixels, A, or B)
     * @return a BufferedImage representing the frame
     */
    public BufferedImage toBufferedImage(byte[][] frame) {

        BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {

                byte idx = frame[x][y];

                // Map byte index → theme color
                Color c = switch (idx) {
                    case FG        -> ThemeManager.get().fg;
                    case PRIMARY   -> ThemeManager.get().primary;
                    case SECONDARY -> ThemeManager.get().secondary;
                    case ACCENT    -> ThemeManager.get().accent;
                    default        -> ThemeManager.get().bg;
                };

                // Convert JavaFX Color → ARGB int
                int argb =
                        ((int) (c.getOpacity() * 255) << 24) |
                                ((int) (c.getRed()     * 255) << 16) |
                                ((int) (c.getGreen()   * 255) <<  8) |
                                (int) (c.getBlue()    * 255);

                img.setRGB(x, y, argb);
            }
        }

        return img;
    }
}
