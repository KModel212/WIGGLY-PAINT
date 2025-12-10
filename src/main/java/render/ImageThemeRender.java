package render;

import javafx.scene.image.*;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

/**
 * Utility class for recoloring pixel-art icons according to the active theme.
 * <p>
 * The recoloring process maps each pixel of a source sprite to whichever of the
 * predefined source palette colors it is closest to, then replaces it with the
 * corresponding color from the current theme:
 *
 * <pre>
 * Source Color    →  Theme Mapping
 * ----------------------------------------
 * #000000 (FG)    →  theme.fg
 * #FFFFFF (BG)    →  theme.bg
 * #FF0097         →  theme.primary
 * #0097FF         →  theme.secondary
 * #FFFF00         →  theme.accent
 * </pre>
 *
 * This approach preserves pixel-art crispness and avoids color bleeding, making
 * it ideal for recoloring monochrome or palette-based icons.
 */
public class ImageThemeRender {

    // ============================================================
    // Reference Colors (source palette)
    // ============================================================

    /** Fixed source palette used to detect which color a pixel should map to. */
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

    /**
     * Retrieves the current theme colors in the same order
     * as {@link #SRC_COLORS} for easy index-based mapping.
     *
     * @return array of theme colors corresponding to FG, BG, PRIMARY, SECONDARY, ACCENT
     */
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

    /**
     * Recolors a pixel-art image by:
     * <ol>
     *     <li>Reading each pixel</li>
     *     <li>Finding the nearest source palette color</li>
     *     <li>Replacing it with the corresponding theme color</li>
     * </ol>
     *
     * Transparent pixels are preserved exactly.
     *
     * @param src original pixel-art image
     * @return a new {@link Image} recolored using the active theme
     */
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

    /**
     * Determines which source palette color is the closest match
     * to a given pixel color using Euclidean RGB distance.
     *
     * @param c the pixel color being matched
     * @return index in {@link #SRC_COLORS} of the closest color
     */
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

    /**
     * Computes the squared Euclidean distance between two colors in RGB space.
     * Alpha is ignored because recoloring applies only to opaque pixels.
     *
     * @param a first color
     * @param b second color
     * @return squared distance (no sqrt needed for comparisons)
     */
    private static double colorDistance(Color a, Color b) {
        double dr = a.getRed()   - b.getRed();
        double dg = a.getGreen() - b.getGreen();
        double db = a.getBlue()  - b.getBlue();
        return dr * dr + dg * dg + db * db;
    }
}
