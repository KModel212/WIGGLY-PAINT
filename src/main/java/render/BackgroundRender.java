package render;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import utils.config.Config;
import utils.themes.ThemeManager;

public class BackgroundRender {

    private static Background currentBackground;   // cached
    private static int lastSpacing;
    private static double lastRadius;

    static {
        // Listen for theme changes → rebuild background tile
        ThemeManager.addListener(BackgroundRender::refreshTheme);
    }

    /** Called when theme changes */
    private static void refreshTheme() {
        // force background regeneration next time someone calls dotBackgroundFromConfig()
        currentBackground = null;
    }

    // ----------------------------------------------------------
    // PUBLIC ENTRY POINT
    // ----------------------------------------------------------
    public static Background dotBackgroundFromConfig() {

        int spacing = Config.getInt("theme.dot.spacing");
        double radius = Config.getDouble("theme.dot.radius");

        // If background is already up to date → reuse cached
        if (currentBackground != null &&
                spacing == lastSpacing &&
                radius == lastRadius)
        {
            return currentBackground;
        }

        // Generate new background
        lastSpacing = spacing;
        lastRadius = radius;

        Color dotColor = ThemeManager.get().fg;
        Color bgColor  = ThemeManager.get().bg;

        currentBackground = dotBackground(spacing, radius, dotColor, bgColor);
        return currentBackground;
    }

    // ----------------------------------------------------------
    // Generate dot tile
    // ----------------------------------------------------------
    public static Background dotBackground(int spacing, double dotRadius, Color dotColor, Color bgColor) {

        Image tile = createDotTile(spacing, dotRadius, dotColor, bgColor);

        ImagePattern pattern = new ImagePattern(
                tile,
                0, 0,
                spacing, spacing,
                false    // repeat tile
        );

        return new Background(
                new BackgroundFill(pattern, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)
        );
    }

    // ----------------------------------------------------------
    // Create tile image
    // ----------------------------------------------------------
    private static Image createDotTile(int size, double dotRadius, Color dotColor, Color bgColor) {

        Canvas tileCanvas = new Canvas(size, size);
        GraphicsContext gc = tileCanvas.getGraphicsContext2D();

        // background
        gc.setFill(bgColor);
        gc.fillRect(0, 0, size, size);

        // dot in center
        gc.setFill(dotColor);
        gc.fillOval(
                size / 2.0 - dotRadius,
                size / 2.0 - dotRadius,
                dotRadius * 2,
                dotRadius * 2
        );

        WritableImage img = new WritableImage(size, size);
        tileCanvas.snapshot(null, img);

        return img;
    }
}
