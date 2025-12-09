package canvas;

import gui.CanvasPane;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

import java.awt.image.BufferedImage;

public class CanvasData {

    // 5 color indices
    public static final byte BG        = 0;
    public static final byte FG        = 1;
    public static final byte PRIMARY   = 2;
    public static final byte SECONDARY = 3;
    public static final byte ACCENT    = 4;

    private static final int SIZE = 200;

    // The framebuffer: 200 x 200 pixels, each storing a value 0–4
    private final byte[][] pixels = new byte[SIZE][SIZE];
    public byte[][] wiggleFrameA = new byte[SIZE][SIZE];
    public byte[][] wiggleFrameB = new byte[SIZE][SIZE];

    public CanvasData() {
        initWiggleFrames(BG);      // Initialize using PRIMARY (or BG)
    }


    private void initWiggleFrames(byte color) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                wiggleFrameA[x][y] = color;
                wiggleFrameB[x][y] = color;
            }
        }
    }

    public int getSize() {
        return SIZE;
    }

    public byte[][] raw() {
        return pixels;
    }
    public byte[][] A() {
        return wiggleFrameA;
    }
    public byte[][] B() {
        return wiggleFrameB;
    }

    // WRITE pixel
    public void set(int layeridx, int x, int y, byte index) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return;
        switch (layeridx){
            case 0: pixels[x][y] = index; break;
            case 1: wiggleFrameA[x][y] = index; break;
            case 2: wiggleFrameB[x][y] = index; break;
            default: break;
        }
    }

    // CLEAR all framebuffer + wiggle frames
    public void clearAll(byte color) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                pixels[x][y] = color;
                wiggleFrameA[x][y] = color;
                wiggleFrameB[x][y] = color;
            }
        }
    }


    public BufferedImage toBufferedImage(byte[][] frame) {
        BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                byte idx = frame[x][y];
                Color c = switch (idx) {
                    case FG -> ThemeManager.get().fg;
                    case PRIMARY -> ThemeManager.get().primary;
                    case SECONDARY -> ThemeManager.get().secondary;
                    case ACCENT -> ThemeManager.get().accent;
                    default -> ThemeManager.get().bg;
                };

                int argb =
                        ((int)(c.getOpacity()*255) << 24) |
                                ((int)(c.getRed()*255) << 16) |
                                ((int)(c.getGreen()*255) << 8) |
                                (int)(c.getBlue()*255);

                img.setRGB(x, y, argb);
            }
        }
        return img;
    }

}
