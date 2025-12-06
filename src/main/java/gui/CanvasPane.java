package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import utils.themes.ThemeManager;

public class CanvasPane extends Pane {

    private int canvasSize = 500;

    public final Canvas layer0 = new Canvas(canvasSize, canvasSize);  // main canvas
    public final Canvas layer1 = new Canvas(canvasSize, canvasSize);  // preview
    public final Canvas layer2 = new Canvas(canvasSize, canvasSize);  // overlays

    private final StackPane layerPane = new StackPane();

    public CanvasPane() {

        layer0.getGraphicsContext2D().setImageSmoothing(false);
        layer1.getGraphicsContext2D().setImageSmoothing(false);
        layer2.getGraphicsContext2D().setImageSmoothing(false);

        // Add canvases to the stack pane
        layerPane.getChildren().addAll(layer0, layer1, layer2);

        // Add the stack pane to this Pane
        getChildren().add(layerPane);

        // --- NEW: initialize background ---
        initBackground();

    }

    private void initBackground() {
        var gc = layer0.getGraphicsContext2D();

        // Background fill
        Color bg = ThemeManager.get().bg;
        gc.setFill(bg);
        gc.fillRect(0, 0, canvasSize, canvasSize);

        // ---- NEW: draw black border ----
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeRect(0, 0, canvasSize, canvasSize);
    }

    public int getCanvasSize() {return canvasSize;}
}
