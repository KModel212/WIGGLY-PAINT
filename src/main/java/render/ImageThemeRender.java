package render;

import canvas.CanvasData;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

/**
 * Recolor pixel-art icons by mapping colors to the nearest of:
 *
 *  #000000 → FG
 *  #FFFFFF → BG
 *  #FF0097 → PRIMARY
 *  #0097FF → SECONDARY
 *  #FFFF00 → ACCENT
 */
public class ImageThemeRender {

    // fixed palette (source colors)
    private static final Color[] SRC_COLORS = {
            Color.web("#000000"), // BLACK → FG
            Color.web("#FFFFFF"), // WHITE → BG
            Color.web("#FF0097"), // PRIMARY
            Color.web("#0097FF"), // SECONDARY
            Color.web("#FFFF00")  // ACCENT
    };

    private static final Color[] THEME_COLORS = {
            ThemeManager.get().fg,
            ThemeManager.get().bg,
            ThemeManager.get().primary,
            ThemeManager.get().secondary,
            ThemeManager.get().accent
    };

    public static Image recolor(Image src) {
        int w = (int) src.getWidth();
        int h = (int) src.getHeight();

        PixelReader reader = src.getPixelReader();
        WritableImage out = new WritableImage(w, h);
        PixelWriter writer = out.getPixelWriter();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {

                Color c = reader.getColor(x, y);

                // preserve transparency
                if (c.getOpacity() == 0.0) {
                    writer.setColor(x, y, c);
                    continue;
                }

                // find closest palette color
                int ci = closestColorIndex(c);

                // recolor using theme mapping
                writer.setColor(x, y, THEME_COLORS[ci]);
            }
        }

        return out;
    }

    /** Compute closest color using Euclidean RGB distance */
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

    /** Euclidean RGB distance */
    private static double colorDistance(Color a, Color b) {
        double dr = a.getRed()   - b.getRed();
        double dg = a.getGreen() - b.getGreen();
        double db = a.getBlue()  - b.getBlue();
        return dr*dr + dg*dg + db*db; // no sqrt needed
    }
}
