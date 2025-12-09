package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import utils.themes.ThemeManager;

public class CanvasPane extends Pane {

    // ============================================================
    // Canvas sizes
    // ============================================================
    private static final int internalSize = 200;   // logical pixel buffer
    private static final int displaySize  = 500;   // scaled display size

    // ============================================================
    // Layers
    // ------------------------------------------------------------
    // layer0 → background (theme-colored)
    // layer1 → wiggle frame A
    // layer2 → wiggle frame B
    // layer3 → cursor / input
    // ============================================================
    public final Canvas layer0 = new Canvas(internalSize, internalSize);
    public final Canvas layer1 = new Canvas(internalSize, internalSize);
    public final Canvas layer2 = new Canvas(internalSize, internalSize);
    public final Canvas layer3 = new Canvas(internalSize, internalSize);

    private final StackPane layerPane = new StackPane();


    // ============================================================
    // Constructor
    // ============================================================
    public CanvasPane() {

        // Ensure pixel art stays crisp
        layer0.getGraphicsContext2D().setImageSmoothing(false);
        layer1.getGraphicsContext2D().setImageSmoothing(false);
        layer2.getGraphicsContext2D().setImageSmoothing(false);
        layer3.getGraphicsContext2D().setImageSmoothing(false);

        // Stack layers from bottom to top
        layerPane.getChildren().addAll(layer0, layer1, layer2, layer3);

        // Scaling from internal pixel size → display pixel size
        double scale = displaySize / (double) internalSize;
        layerPane.setScaleX(scale);
        layerPane.setScaleY(scale);
        layerPane.setPrefSize(displaySize, displaySize);

        // Add container
        setPrefSize(displaySize, displaySize);
        getChildren().add(layerPane);

        // Draw initial background
        redrawBackground();

        // Auto-update when theme changes
        ThemeManager.addListener(this::redrawBackground);
    }


    // ============================================================
    // Theme background redraw
    // ============================================================
    private void redrawBackground() {

        var gc = layer0.getGraphicsContext2D();

        // background fill
        gc.setFill(ThemeManager.get().bg);
        gc.fillRect(0, 0, internalSize, internalSize);

        // border outline
        gc.setStroke(ThemeManager.get().fg);
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, internalSize, internalSize);
    }


    // ============================================================
    // Coordinate conversion (display → internal)
    // ============================================================
    public double toInternalX(double x) {
        return x * internalSize / displaySize;
    }

    public double toInternalY(double y) {
        return y * internalSize / displaySize;
    }


    // ============================================================
    // Accessors
    // ============================================================
    public int getInternalSize() { return internalSize; }
    public int getDisplaySize()  { return displaySize; }
    public int getCanvasSize()   { return internalSize; }

    public Canvas getCursorLayer() {
        return layer3;
    }


    // ============================================================
    // Cursor helper
    // ============================================================
    public void clearCursor() {
        layer3.getGraphicsContext2D().clearRect(0, 0, internalSize, internalSize);
    }
}
