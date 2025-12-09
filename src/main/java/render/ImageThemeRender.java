package render;

import javafx.scene.image.*;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

/**
 * Recolor pixel-art icons by mapping to theme colors.
 *
 * Source colors:
 *   #000000 → FG
 *   #FFFFFF → BG
 *   #FF0097 → PRIMARY
 *   #0097FF → SECONDARY
 *   #FFFF00 → ACCENT
 *
 * Each pixel in the source image is mapped to whichever source color
 * it is closest to, then recolored to the corresponding theme color.
 */
public class ImageThemeRender {

    // ============================================================
    // Reference Colors (source palette)
    // ============================================================
    private static final Color[] SRC_COLORS = {
            Color.web("#000000"), // FG
            Color.web("#FFFFFF"), // BG
            Color.web("#FF0097"), // PRIMARY
            Color.web("#0097FF"), // SECONDARY
            Color.web("#FFFF00")  // ACCENT
    };


    // ============================================================
    // Fetch fresh theme colors each time
    // ============================================================
    private static Color[] getThemeColors() {
        return new Color[] {
                ThemeManager.get().fg,
                ThemeManager.get().bg,
                ThemeManager.get().primary,
                ThemeManager.get().secondary,
                ThemeManager.get().accent
        };
    }


    // ============================================================
    // Recolor an image using the nearest palette match
    // ============================================================
    public static Image recolor(Image src) {

        int w = (int) src.getWidth();
        int h = (int) src.getHeight();

        PixelReader reader = src.getPixelReader();
        WritableImage output = new WritableImage(w, h);
        PixelWriter writer = output.getPixelWriter();

        Color[] theme = getThemeColors();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {

                Color c = reader.getColor(x, y);

                // Preserve transparent pixels
                if (c.getOpacity() == 0.0) {
                    writer.setColor(x, y, c);
                    continue;
                }

                int idx = closestColorIndex(c);
                writer.setColor(x, y, theme[idx]);
            }
        }

        return output;
    }


    // ============================================================
    // Find nearest source palette color index
    // ============================================================
    private static int closestColorIndex(Color c) {

        int bestIndex = 0;
        double bestDist = Double.MAX_VALUE;

        for (int i = 0; i < SRC_COLORS.length; i++) {
            double d = colorDistance(c, SRC_COLORS[i]);
            if (d < bestDist) {
                bestDist = d;
                bestIndex = i;
            }
        }
        return bestIndex;
    }


    // ============================================================
    // Euclidean RGB distance
    // ============================================================
    private static double colorDistance(Color a, Color b) {
        double dr = a.getRed()   - b.getRed();
        double dg = a.getGreen() - b.getGreen();
        double db = a.getBlue()  - b.getBlue();
        return dr * dr + dg * dg + db * db;
    }
}
