package canvas;

import gui.CanvasPane;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

import java.awt.image.BufferedImage;

public class CanvasData {

    // ============================================================
    // Color indices
    // ============================================================
    public static final byte BG        = 0;
    public static final byte FG        = 1;
    public static final byte PRIMARY   = 2;
    public static final byte SECONDARY = 3;
    public static final byte ACCENT    = 4;

    private static final int SIZE = 200;


    // ============================================================
    // Framebuffers
    // ------------------------------------------------------------
    // pixels        : base layer
    // wiggleFrameA  : frame 1 of wiggle animation
    // wiggleFrameB  : frame 2 of wiggle animation
    // ============================================================
    private final byte[][] pixels       = new byte[SIZE][SIZE];
    public final byte[][] wiggleFrameA  = new byte[SIZE][SIZE];
    public final byte[][] wiggleFrameB  = new byte[SIZE][SIZE];


    // ============================================================
    // Constructor
    // ============================================================
    public CanvasData() {
        initWiggleFrames(BG);
    }


    // ============================================================
    // Initialization
    // ============================================================
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
    public int getSize() { return SIZE; }

    public byte[][] raw() { return pixels; }
    public byte[][] A()   { return wiggleFrameA; }
    public byte[][] B()   { return wiggleFrameB; }


    // ============================================================
    // Write a pixel into the specified layer
    // layeridx:
    //   0 → base pixels
    //   1 → wiggle frame A
    //   2 → wiggle frame B
    // ============================================================
    public void set(int layeridx, int x, int y, byte index) {

        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return;

        switch (layeridx) {
            case 0 -> pixels[x][y]      = index;
            case 1 -> wiggleFrameA[x][y] = index;
            case 2 -> wiggleFrameB[x][y] = index;
            default -> { /* ignore invalid layers */ }
        }
    }


    // ============================================================
    // Clear all layers
    // ============================================================
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
    // Convert frame buffer → BufferedImage
    // Used for export GIF / screenshot
    // ============================================================
    public BufferedImage toBufferedImage(byte[][] frame) {

        BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {

                byte idx = frame[x][y];

                Color c = switch (idx) {
                    case FG        -> ThemeManager.get().fg;
                    case PRIMARY   -> ThemeManager.get().primary;
                    case SECONDARY -> ThemeManager.get().secondary;
                    case ACCENT    -> ThemeManager.get().accent;
                    default        -> ThemeManager.get().bg;
                };

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
