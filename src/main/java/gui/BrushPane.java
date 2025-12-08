package gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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

        // Load all icons
        pencilIcon     = loadIcon("/brush/pencil.PNG");
        fountainIcon   = loadIcon("/brush/fountain.PNG");
        highlightAIcon = loadIcon("/brush/highlightA.PNG");
        highlightBIcon = loadIcon("/brush/highlightB.PNG");
        highlightCIcon = loadIcon("/brush/highlightC.PNG");
        markerIcon     = loadIcon("/brush/marker.PNG");
        sprayIcon      = loadIcon("/brush/spray.PNG");
        eraserIcon     = loadIcon("/brush/eraser.PNG");

        // Add into pane
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

    private ImageView loadIcon(String path) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(path)));
        icon.setFitWidth(200);
        icon.setPreserveRatio(true);
        return icon;
    }
}
