//package gui;
//
//import javafx.geometry.Pos;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.VBox;
//
//public class BrushPane extends VBox {
//
//    public final ImageView pencilIcon;
//    public final ImageView fountainIcon;
//    public final ImageView highlightAIcon;
//    public final ImageView highlightBIcon;
//    public final ImageView highlightCIcon;
//    public final ImageView markerIcon;
//    public final ImageView sprayIcon;
//    public final ImageView eraserIcon;
//
//    public BrushPane() {
//        super(12);
//        this.setAlignment(Pos.CENTER_RIGHT);
//
//        // Load all icons
//        pencilIcon     = loadIcon("/brush/pencil.PNG");
//        fountainIcon   = loadIcon("/brush/fountain.PNG");
//        highlightAIcon = loadIcon("/brush/highlightA.PNG");
//        highlightBIcon = loadIcon("/brush/highlightB.PNG");
//        highlightCIcon = loadIcon("/brush/highlightC.PNG");
//        markerIcon     = loadIcon("/brush/marker.PNG");
//        sprayIcon      = loadIcon("/brush/spray.PNG");
//        eraserIcon     = loadIcon("/brush/eraser.PNG");
//
//        // Add into pane
//        this.getChildren().addAll(
//                pencilIcon,
//                fountainIcon,
//                markerIcon,
//                sprayIcon,
//                highlightAIcon,
//                highlightBIcon,
//                highlightCIcon,
//                eraserIcon
//        );
//    }
//
//    private ImageView loadIcon(String path) {
//        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(path)));
//        icon.setFitWidth(200);
//        icon.setPreserveRatio(true);
//        return icon;
//    }
//}

package gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import render.ImageThemeRender;

public class BrushPane extends VBox {

    public final ImageView pencilIcon;
    public final ImageView fountainIcon;
    public final ImageView highlightAIcon;
    public final ImageView highlightBIcon;
    public final ImageView highlightCIcon;
    public final ImageView markerIcon;
    public final ImageView sprayIcon;
    public final ImageView eraserIcon;

    public BrushPane() {
        super(12);
        this.setAlignment(Pos.CENTER_RIGHT);

        // Load & recolor icons
        pencilIcon     = loadAndRecolor("/brush/pencil.PNG");
        fountainIcon   = loadAndRecolor("/brush/fountain.PNG");
        highlightAIcon = loadAndRecolor("/brush/highlightA.PNG");
        highlightBIcon = loadAndRecolor("/brush/highlightB.PNG");
        highlightCIcon = loadAndRecolor("/brush/highlightC.PNG");
        markerIcon     = loadAndRecolor("/brush/marker.PNG");
        sprayIcon      = loadAndRecolor("/brush/spray.PNG");
        eraserIcon     = loadAndRecolor("/brush/eraser.PNG");

        // Add all icons to VBox
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
    }


    private ImageView loadAndRecolor(String path) {
        Image raw = new Image(getClass().getResourceAsStream(path));
        Image colored = ImageThemeRender.recolor(raw);  // ★ APPLY THEME COLOR

        ImageView icon = new ImageView(colored);
        icon.setFitWidth(200);
        icon.setPreserveRatio(true);

        return icon;
    }


    public void refreshTheme() {
        pencilIcon.setImage(ImageThemeRender.recolor(pencilIcon.getImage()));
        fountainIcon.setImage(ImageThemeRender.recolor(fountainIcon.getImage()));
        highlightAIcon.setImage(ImageThemeRender.recolor(highlightAIcon.getImage()));
        highlightBIcon.setImage(ImageThemeRender.recolor(highlightBIcon.getImage()));
        highlightCIcon.setImage(ImageThemeRender.recolor(highlightCIcon.getImage()));
        markerIcon.setImage(ImageThemeRender.recolor(markerIcon.getImage()));
        sprayIcon.setImage(ImageThemeRender.recolor(sprayIcon.getImage()));
        eraserIcon.setImage(ImageThemeRender.recolor(eraserIcon.getImage()));
    }
}


