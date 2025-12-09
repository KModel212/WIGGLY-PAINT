package grader;

import canvas.CanvasData;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.themes.ThemeManager;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class CanvasDataTest {

    private CanvasData data;

    @BeforeEach
    void setup() {
        // Set fixed theme for predictable colors
        ThemeManager.setTheme("classic");   // must exist in your properties

        data = new CanvasData();
        data.clearAll(CanvasData.BG);
    }


    // ------------------------------------------------------------
    // 1. Test set() writes correct layers
    // ------------------------------------------------------------
    @Test
    void testSetWritesToBaseLayer() {
        data.set(0, 10, 10, CanvasData.FG);
        assertEquals(CanvasData.FG, data.raw()[10][10]);
    }

    @Test
    void testSetWritesToFrameA() {
        data.set(1, 20, 20, CanvasData.PRIMARY);
        assertEquals(CanvasData.PRIMARY, data.A()[20][20]);
    }

    @Test
    void testSetWritesToFrameB() {
        data.set(2, 30, 30, CanvasData.ACCENT);
        assertEquals(CanvasData.ACCENT, data.B()[30][30]);
    }


    // ------------------------------------------------------------
    // 2. Out-of-bounds writes must be ignored
    // ------------------------------------------------------------
    @Test
    void testSetIgnoresOutOfBounds() {
        data.set(0, -1, -1, CanvasData.FG);
        data.set(0, 500, 500, CanvasData.FG);

        // Should remain BG (clearAll sets everything to BG)
        assertEquals(CanvasData.BG, data.raw()[0][0]);
        assertEquals(CanvasData.BG, data.raw()[199][199]);
    }


    // ------------------------------------------------------------
    // 3. clearAll must fill all framebuffers
    // ------------------------------------------------------------
    @Test
    void testClearAll() {
        data.set(0, 10, 10, CanvasData.ACCENT);
        data.set(1, 11, 11, CanvasData.FG);
        data.set(2, 12, 12, CanvasData.SECONDARY);

        data.clearAll(CanvasData.PRIMARY);

        assertEquals(CanvasData.PRIMARY, data.raw()[10][10]);
        assertEquals(CanvasData.PRIMARY, data.A()[11][11]);
        assertEquals(CanvasData.PRIMARY, data.B()[12][12]);
    }


    // ------------------------------------------------------------
    // 4. Test toBufferedImage color conversion
    // ------------------------------------------------------------
    @Test
    void testToBufferedImageColorConversion() {
        // paint 1 pixel with FG
        data.set(0, 5, 5, CanvasData.FG);

        BufferedImage img = data.toBufferedImage(data.raw());

        Color themeColor = ThemeManager.get().fg;

        int expectedARGB =
                ((int) (themeColor.getOpacity() * 255) << 24) |
                        ((int) (themeColor.getRed()     * 255) << 16) |
                        ((int) (themeColor.getGreen()   * 255) <<  8) |
                        (int) (themeColor.getBlue()    * 255);

        assertEquals(expectedARGB, img.getRGB(5, 5));
    }


    // ------------------------------------------------------------
    // 5. Frame sizes must be correct
    // ------------------------------------------------------------
    @Test
    void testFrameSizes() {
        assertEquals(200, data.getSize());
        assertEquals(200, data.raw().length);
        assertEquals(200, data.A().length);
        assertEquals(200, data.B().length);

        assertEquals(200, data.raw()[0].length);
        assertEquals(200, data.A()[0].length);
        assertEquals(200, data.B()[0].length);
    }
}
