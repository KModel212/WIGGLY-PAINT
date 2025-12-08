package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

public class CanvasPane extends Pane {

    // Internal canvas resolution
    private static final int internalSize = 200;

    // Display canvas size
    private static int displaySize = 500;

    // Layers:
    // 0 = background
    // 1 = drawing
    // 2 = overlay
    // 3 = mouse input
    public final Canvas layer0 = new Canvas(internalSize, internalSize);
    public final Canvas layer1 = new Canvas(internalSize, internalSize);
    public final Canvas layer2 = new Canvas(internalSize, internalSize);
    public final Canvas layer3 = new Canvas(internalSize, internalSize);


    private final StackPane layerPane = new StackPane();

    public CanvasPane() {

        // Crisp pixel-art rendering
        layer0.getGraphicsContext2D().setImageSmoothing(false);
        layer1.getGraphicsContext2D().setImageSmoothing(false);
        layer2.getGraphicsContext2D().setImageSmoothing(false);
        layer3.getGraphicsContext2D().setImageSmoothing(false);

        // Stack layers in correct order
        layerPane.getChildren().addAll(layer0, layer1, layer2, layer3);

        // Scale to display size
        double scale = displaySize / (double) internalSize;
        layerPane.setScaleX(scale);
        layerPane.setScaleY(scale);
        layerPane.setPrefSize(displaySize, displaySize);

        setPrefSize(displaySize, displaySize);
        getChildren().add(layerPane);

        // Draw background one time
        initBackground();
    }

    /** Draw static background on layer0 */
    private void initBackground() {
//        var gc = layer0.getGraphicsContext2D();
//
//        Color bg = ThemeManager.get().bg;
//
//        gc.setFill(bg);
//        gc.fillRect(0, 0, internalSize, internalSize);
//
//        gc.setStroke(Color.BLACK);
//        gc.setLineWidth(1);
//        gc.strokeRect(0, 0, internalSize, internalSize);
    }

    // Coordinate conversion (display → internal pixel index)
    public double toInternalX(double x) {
        return x * internalSize / displaySize;
    }

    public double toInternalY(double y) {
        return y * internalSize / displaySize;
    }

    public int getInternalSize() { return internalSize; }
    public int getDisplaySize() { return displaySize; }
    public int getCanvasSize() { return internalSize; }
}
