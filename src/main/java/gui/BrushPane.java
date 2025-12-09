package gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import render.ImageThemeRender;
import utils.themes.ThemeManager;

import java.util.List;

public class BrushPane extends VBox {

    // original un-recolored icons
    private Image pencilSrc;
    private Image fountainSrc;
    private Image highlightASrc;
    private Image highlightBSrc;
    private Image highlightCSrc;
    private Image markerSrc;
    private Image spraySrc;
    private Image eraserSrc;

    // displayed icons
    public ImageView pencilIcon;
    public ImageView fountainIcon;
    public ImageView highlightAIcon;
    public ImageView highlightBIcon;
    public ImageView highlightCIcon;
    public ImageView markerIcon;
    public ImageView sprayIcon;
    public ImageView eraserIcon;

    public BrushPane() {
        super(12);
        this.setAlignment(Pos.CENTER_RIGHT);

        // Load source icons ONCE
        pencilSrc     = new Image(getClass().getResourceAsStream("/brush/pencil.PNG"));
        fountainSrc   = new Image(getClass().getResourceAsStream("/brush/fountain.PNG"));
        highlightASrc = new Image(getClass().getResourceAsStream("/brush/highlightA.PNG"));
        highlightBSrc = new Image(getClass().getResourceAsStream("/brush/highlightB.PNG"));
        highlightCSrc = new Image(getClass().getResourceAsStream("/brush/highlightC.PNG"));
        markerSrc     = new Image(getClass().getResourceAsStream("/brush/marker.PNG"));
        spraySrc      = new Image(getClass().getResourceAsStream("/brush/spray.PNG"));
        eraserSrc     = new Image(getClass().getResourceAsStream("/brush/eraser.PNG"));

        // Create themed icons
        pencilIcon     = makeIcon(pencilSrc);
        fountainIcon   = makeIcon(fountainSrc);
        highlightAIcon = makeIcon(highlightASrc);
        highlightBIcon = makeIcon(highlightBSrc);
        highlightCIcon = makeIcon(highlightCSrc);
        markerIcon     = makeIcon(markerSrc);
        sprayIcon      = makeIcon(spraySrc);
        eraserIcon     = makeIcon(eraserSrc);

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

        // 🔥 Theme auto-update
        ThemeManager.addListener(this::refreshTheme);
    }

    private ImageView makeIcon(Image raw) {
        Image themed = ImageThemeRender.recolor(raw);
        ImageView v = new ImageView(themed);
        v.setFitWidth(200);
        v.setPreserveRatio(true);
        return v;
    }

    public List<ImageView> getAllIcons() {
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

    private Runnable onThemeRefreshed;

    public void setOnThemeRefreshed(Runnable callback) {
        this.onThemeRefreshed = callback;
    }

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
            default -> null;
        };
    }



    /** Recolor ALL icons using the original source images */
    public void refreshTheme() {

        // rebuild themed icons
        pencilIcon     = makeIcon(pencilSrc);
        fountainIcon   = makeIcon(fountainSrc);
        highlightAIcon = makeIcon(highlightASrc);
        highlightBIcon = makeIcon(highlightBSrc);
        highlightCIcon = makeIcon(highlightCSrc);
        markerIcon     = makeIcon(markerSrc);
        sprayIcon      = makeIcon(spraySrc);
        eraserIcon     = makeIcon(eraserSrc);

        // rebuild layout
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

        if (onThemeRefreshed != null) {
            onThemeRefreshed.run();
        }
    }


}
