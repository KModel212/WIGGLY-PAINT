package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

public class CanvasPane extends Pane {

    private static final int internalSize = 200;
    private static int displaySize = 500;

    public final Canvas layer0 = new Canvas(internalSize, internalSize);
    public final Canvas layer1 = new Canvas(internalSize, internalSize);
    public final Canvas layer2 = new Canvas(internalSize, internalSize);
    public final Canvas layer3 = new Canvas(internalSize, internalSize);

    private final StackPane layerPane = new StackPane();


    public CanvasPane() {

        // crisp pixels
        layer0.getGraphicsContext2D().setImageSmoothing(false);
        layer1.getGraphicsContext2D().setImageSmoothing(false);
        layer2.getGraphicsContext2D().setImageSmoothing(false);
        layer3.getGraphicsContext2D().setImageSmoothing(false);

        layerPane.getChildren().addAll(layer0, layer1, layer2, layer3);

        double scale = displaySize / (double) internalSize;
        layerPane.setScaleX(scale);
        layerPane.setScaleY(scale);
        layerPane.setPrefSize(displaySize, displaySize);

        setPrefSize(displaySize, displaySize);
        getChildren().add(layerPane);

        // draw first theme background
        redrawBackground();

        // 🌈 React when theme changes
        ThemeManager.addListener(this::redrawBackground);
    }


    // ----------------------------------------------------
    // THEME HANDLER (called on theme change)
    // ----------------------------------------------------
    private void redrawBackground() {

        var gc = layer0.getGraphicsContext2D();

        // background
        gc.setFill(ThemeManager.get().bg);
        gc.fillRect(0, 0, internalSize, internalSize);

        // border
        gc.setStroke(ThemeManager.get().fg);
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, internalSize, internalSize);
    }


    // ----------------------------------------------------
    // COORDINATE CONVERSION
    // ----------------------------------------------------
    public double toInternalX(double x) {
        return x * internalSize / displaySize;
    }

    public double toInternalY(double y) {
        return y * internalSize / displaySize;
    }

    public int getInternalSize() { return internalSize; }
    public int getDisplaySize() { return displaySize; }
    public int getCanvasSize() { return internalSize; }

    public Canvas getCursorLayer() { return layer3; }

    public void clearCursor() {
        layer3.getGraphicsContext2D().clearRect(0, 0, internalSize, internalSize);
    }
}
