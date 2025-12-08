package render;

import canvas.CanvasData;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

/**
 * Recolor pixel-art icons by mapping specific source colors
 * to theme colors:
 *
 *  #000000 → FG
 *  #FFFFFF → BG
 *  #FF0097 → PRIMARY
 *  #0097FF → SECONDARY
 *  #FFFF00 → ACCENT
 *
 * Supports transparency and preserves crisp edges.
 */
public class ImageThemeRender {

    private static final Color SRC_BLACK    = Color.web("#FFFFFF");
    private static final Color SRC_WHITE    = Color.web("#000000");
    private static final Color SRC_PRIMARY  = Color.web("#FF0097");
    private static final Color SRC_SECOND   = Color.web("#0097FF");
    private static final Color SRC_ACCENT   = Color.web("#FFFF00");

    public static Image recolor(Image src) {
        int w = (int) src.getWidth();
        int h = (int) src.getHeight();

        PixelReader reader = src.getPixelReader();
        WritableImage out = new WritableImage(w, h);
        PixelWriter writer = out.getPixelWriter();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Color c = reader.getColor(x, y);

                // keep transparent pixels
                if (c.getOpacity() == 0.0) {
                    writer.setColor(x, y, c);
                    continue;
                }

                // exact color matching
                if (equalsColor(c, SRC_BLACK)) {
                    writer.setColor(x, y, ThemeManager.get().fg);
                }
                else if (equalsColor(c, SRC_WHITE)) {
                    writer.setColor(x, y, ThemeManager.get().bg);
                }
                else if (equalsColor(c, SRC_PRIMARY)) {
                    writer.setColor(x, y, ThemeManager.get().primary);
                }
                else if (equalsColor(c, SRC_SECOND)) {
                    writer.setColor(x, y, ThemeManager.get().secondary);
                }
                else if (equalsColor(c, SRC_ACCENT)) {
                    writer.setColor(x, y, ThemeManager.get().accent);
                }
                else {
                    // if a color doesn't match exactly, keep it as-is
                    writer.setColor(x, y, c);
                }
            }
        }

        return out;
    }

    // Helper: compare pixel colors exactly (avoid floating errors)
    private static boolean equalsColor(Color a, Color b) {
        return (int)(a.getRed()*255)   == (int)(b.getRed()*255) &&
                (int)(a.getGreen()*255) == (int)(b.getGreen()*255) &&
                (int)(a.getBlue()*255)  == (int)(b.getBlue()*255);
    }
}
