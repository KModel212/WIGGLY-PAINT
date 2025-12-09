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

    // ============================================================
    // Cached background
    // ============================================================
    private static Background currentBackground;
    private static int lastSpacing;
    private static double lastRadius;

    // Register theme listener → theme change invalidates cache
    static {
        ThemeManager.addListener(BackgroundRender::refreshTheme);
    }


    // ============================================================
    // Theme refresh callback
    // ============================================================
    private static void refreshTheme() {
        currentBackground = null;  // force regeneration next call
    }


    // ============================================================
    // PUBLIC API — get background from config
    // ============================================================
    public static Background dotBackgroundFromConfig() {

        int spacing = Config.getInt("theme.dot.spacing");
        double radius = Config.getDouble("theme.dot.radius");

        // Reuse cached background if still valid
        if (currentBackground != null &&
                spacing == lastSpacing &&
                radius == lastRadius)
        {
            return currentBackground;
        }

        lastSpacing = spacing;
        lastRadius  = radius;

        Color dotColor = ThemeManager.get().fg;
        Color bgColor  = ThemeManager.get().bg;

        currentBackground = dotBackground(spacing, radius, dotColor, bgColor);
        return currentBackground;
    }


    // ============================================================
    // Build repeating dot pattern background
    // ============================================================
    public static Background dotBackground(
            int spacing,
            double dotRadius,
            Color dotColor,
            Color bgColor
    ) {
        Image tile = createDotTile(spacing, dotRadius, dotColor, bgColor);

        ImagePattern pattern = new ImagePattern(
                tile,
                0, 0,
                spacing, spacing,
                false
        );

        return new Background(
                new BackgroundFill(pattern, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)
        );
    }


    // ============================================================
    // Create a tile image containing one centered dot
    // ============================================================
    private static Image createDotTile(
            int size,
            double dotRadius,
            Color dotColor,
            Color bgColor
    ) {
        Canvas tileCanvas = new Canvas(size, size);
        GraphicsContext gc = tileCanvas.getGraphicsContext2D();

        // Background
        gc.setFill(bgColor);
        gc.fillRect(0, 0, size, size);

        // Dot (center)
        gc.setFill(dotColor);
        gc.fillOval(
                size / 2.0 - dotRadius,
                size / 2.0 - dotRadius,
                dotRadius * 2,
                dotRadius * 2
        );

        // Capture tile as image
        WritableImage img = new WritableImage(size, size);
        tileCanvas.snapshot(null, img);

        return img;
    }
}
