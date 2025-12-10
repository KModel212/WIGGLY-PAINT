package gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import render.ImageThemeRender;
import utils.themes.ThemeManager;

import java.util.List;

/**
 * UI panel that displays all brush icons in a vertical toolbar.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Load raw icon images (pixel-art source files)</li>
 *     <li>Recolor icons according to active ThemeManager colors</li>
 *     <li>Expose themed ImageView objects to the BrushController</li>
 *     <li>Rebuild all icons dynamically when the theme changes</li>
 * </ul>
 *
 * The raw icons are stored once, and recolored copies are generated
 * whenever the theme updates. This ensures crisp pixel-art results.
 */
public class BrushPane extends VBox {

    // ============================================================
    // Source icons (original, untinted)
    // ============================================================

    /** Raw original icon for pencil. */
    private Image pencilSrc;
    private Image fountainSrc;
    private Image highlightASrc;
    private Image highlightBSrc;
    private Image highlightCSrc;
    private Image markerSrc;
    private Image spraySrc;
    private Image eraserSrc;

    // ============================================================
    // Displayed icons (theme-colored)
    // ============================================================

    public ImageView pencilIcon;
    public ImageView fountainIcon;
    public ImageView highlightAIcon;
    public ImageView highlightBIcon;
    public ImageView highlightCIcon;
    public ImageView markerIcon;
    public ImageView sprayIcon;
    public ImageView eraserIcon;

    /** Callback used to notify BrushController to rebind icons. */
    private Runnable onThemeRefreshed;


    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Creates a brush toolbar panel that loads source icons,
     * recolors them, displays them in a vertical list,
     * and listens for theme changes.
     */
    public BrushPane() {
        super(12);
        this.setAlignment(Pos.CENTER_RIGHT);

        // Load original pixel-art icons
        pencilSrc     = new Image(getClass().getResourceAsStream("/brush/pencil.PNG"));
        fountainSrc   = new Image(getClass().getResourceAsStream("/brush/fountain.PNG"));
        highlightASrc = new Image(getClass().getResourceAsStream("/brush/highlightA.PNG"));
        highlightBSrc = new Image(getClass().getResourceAsStream("/brush/highlightB.PNG"));
        highlightCSrc = new Image(getClass().getResourceAsStream("/brush/highlightC.PNG"));
        markerSrc     = new Image(getClass().getResourceAsStream("/brush/marker.PNG"));
        spraySrc      = new Image(getClass().getResourceAsStream("/brush/spray.PNG"));
        eraserSrc     = new Image(getClass().getResourceAsStream("/brush/eraser.PNG"));

        // Create recolored ImageViews
        pencilIcon     = makeIcon(pencilSrc);
        fountainIcon   = makeIcon(fountainSrc);
        highlightAIcon = makeIcon(highlightASrc);
        highlightBIcon = makeIcon(highlightBSrc);
        highlightCIcon = makeIcon(highlightCSrc);
        markerIcon     = makeIcon(markerSrc);
        sprayIcon      = makeIcon(spraySrc);
        eraserIcon     = makeIcon(eraserSrc);

        // Add to UI layout
        this.getChildren().addAll(
                pencilIcon,
                fountainIcon,
                markerIcon,
                sprayIcon,
                highlightAIcon,
                highlightBIcon,
                highlightCIcon,
                eraserIcon
        );

        // Recolor icons on theme switch
        ThemeManager.addListener(this::refreshTheme);
    }


    // ============================================================
    // Helper: create an ImageView with recolored theme icon
    // ============================================================

    /**
     * Creates an ImageView by applying theme-based recoloring to a raw image.
     *
     * @param raw original pixel-art source image
     * @return ImageView using recolored version of the icon
     */
    private ImageView makeIcon(Image raw) {
        Image themed = ImageThemeRender.recolor(raw);
        ImageView view = new ImageView(themed);
        view.setFitWidth(200);
        view.setPreserveRatio(true);
        return view;
    }


    // ============================================================
    // Accessors
    // ============================================================

    /**
     * Gets all icon ImageViews in rendering order.
     *
     * @return list of themed brush icons
     */
    private List<ImageView> getAllIcons() {
        return List.of(
                pencilIcon,
                fountainIcon,
                markerIcon,
                sprayIcon,
                highlightAIcon,
                highlightBIcon,
                highlightCIcon,
                eraserIcon
        );
    }

    /**
     * Sets a callback invoked whenever icons are rebuilt due to theme changes.
     *
     * @param callback runnable invoked after rebuild
     */
    public void setOnThemeRefreshed(Runnable callback) {
        this.onThemeRefreshed = callback;
    }

    /**
     * Looks up an ImageView for a given brush name string.
     * Used by BrushController to restore selection.
     *
     * @param name brush identifier string
     * @return corresponding themed icon node, or null if not found
     */
    public ImageView getIconFor(String name) {
        return switch (name) {
            case "pencil"      -> pencilIcon;
            case "fountain"    -> fountainIcon;
            case "marker"      -> markerIcon;
            case "spray"       -> sprayIcon;
            case "highlightA"  -> highlightAIcon;
            case "highlightB"  -> highlightBIcon;
            case "highlightC"  -> highlightCIcon;
            case "eraser"      -> eraserIcon;
            default            -> null;
        };
    }


    // ============================================================
    // Theme Refresh — rebuild icons from original sources
    // ============================================================

    /**
     * Rebuilds all icon ImageViews by recoloring the original images
     * with the new theme colors, then updates the VBox layout.
     * <p>
     * Finally notifies BrushController so it can rebind click handlers
     * and maintain the current brush selection.
     */
    public void refreshTheme() {

        // Rebuild recolored icons
        pencilIcon     = makeIcon(pencilSrc);
        fountainIcon   = makeIcon(fountainSrc);
        highlightAIcon = makeIcon(highlightASrc);
        highlightBIcon = makeIcon(highlightBSrc);
        highlightCIcon = makeIcon(highlightCSrc);
        markerIcon     = makeIcon(markerSrc);
        sprayIcon      = makeIcon(spraySrc);
        eraserIcon     = makeIcon(eraserSrc);

        // Rebuild layout
        this.getChildren().setAll(
                pencilIcon,
                fountainIcon,
                markerIcon,
                sprayIcon,
                highlightAIcon,
                highlightBIcon,
                highlightCIcon,
                eraserIcon
        );

        // Notify BrushController to reconnect its event handlers
        if (onThemeRefreshed != null) {
            onThemeRefreshed.run();
        }
    }
}
