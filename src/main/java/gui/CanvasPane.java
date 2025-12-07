package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

public class CanvasPane extends Pane {

    // internal pixel buffer
    private final int internalSize = 200;

    // visible display size
    private final int displaySize = 500;

    public final Canvas layer0 = new Canvas(internalSize, internalSize);  // background + drawing
    public final Canvas layer1 = new Canvas(internalSize, internalSize);  // preview
    public final Canvas layer2 = new Canvas(internalSize, internalSize);  // overlays

    private final StackPane layerPane = new StackPane();

    public CanvasPane() {

        // Turn off smoothing = crisp pixel-art
        layer0.getGraphicsContext2D().setImageSmoothing(false);
        layer1.getGraphicsContext2D().setImageSmoothing(false);
        layer2.getGraphicsContext2D().setImageSmoothing(false);

        // Put all canvas layers inside stack
        layerPane.getChildren().addAll(layer0, layer1, layer2);

        // SCALE the entire stack from 200 → 500 px
        double scale = displaySize / (double) internalSize;
        layerPane.setScaleX(scale);
        layerPane.setScaleY(scale);

        // Let the pane occupy 500x500
        layerPane.setPrefSize(displaySize, displaySize);
        setPrefSize(displaySize, displaySize);

        getChildren().add(layerPane);

        initBackground();
    }

    private void initBackground() {
        var gc = layer0.getGraphicsContext2D();

        // Fill background
        Color bg = ThemeManager.get().bg;
        gc.setFill(bg);
        gc.fillRect(0, 0, internalSize, internalSize);

        // Black border (drawn on 200x200, scaled to 500)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, internalSize, internalSize);
    }

    /** Converts screen mouse X (0–500) → internal pixel X (0–200) */
    public double toInternalX(double mouseX) {
        return mouseX * internalSize / displaySize;
    }

    /** Converts screen mouse Y (0–500) → internal pixel Y (0–200) */
    public double toInternalY(double mouseY) {
        return mouseY * internalSize / displaySize;
    }

    public int getInternalSize() { return internalSize; }
    public int getDisplaySize() { return displaySize; }


}
