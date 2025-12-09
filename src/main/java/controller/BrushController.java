package controller;

import brush.*;
import gui.BrushPane;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class BrushController {

    private final BrushPane pane;

    // Brush objects
    private final Paintable pencil      = new PencilBrush(1);
    private final Paintable fountain    = new FountainBrush(5 , 0.6);
    private final Paintable marker      = new PencilBrush(3);
    private final Paintable spray       = new SprayBrush(6,0);
    private final Paintable highlightA  = new HighlightBrush(5,2,0);
    private final Paintable highlightB  = new HighlightBrush(5,3,0);
    private final Paintable highlightC  = new HighlightBrush(5,4,0);
    private final Paintable eraser      = new EraserBrush(10);

    private Paintable activeBrush = null;
    private Node currentBrushNode = null;

    public BrushController(BrushPane pane) {
        this.pane = pane;

        // Attach events
        attach(pane.pencilIcon,     pencil);
        attach(pane.fountainIcon,   fountain);
        attach(pane.markerIcon,     marker);
        attach(pane.sprayIcon,      spray);
        attach(pane.highlightAIcon, highlightA);
        attach(pane.highlightBIcon, highlightB);
        attach(pane.highlightCIcon, highlightC);
        attach(pane.eraserIcon,     eraser);

        selectBrush(pencil, pane.pencilIcon);
    }

    private void attach(Node icon, Paintable brush) {
        icon.setOnMouseClicked(e -> selectBrush(brush, icon));
    }

    // ------------------------------
    // Brush Selection Animation
    // ------------------------------
    private void selectBrush(Paintable brush, Node icon) {

        activeBrush = brush;

        // Reset old brush
        if (currentBrushNode != null) {
            animateBack(currentBrushNode);
            currentBrushNode.setEffect(null);
        }

        // Animate new brush
        animateSelect(icon);

        currentBrushNode = icon;
    }

    private void animateSelect(Node node) {
        TranslateTransition out = new TranslateTransition(Duration.millis(200), node);
        out.setToX(-20);
        out.play();

        ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.play();
    }

    private void animateBack(Node node) {
        TranslateTransition back = new TranslateTransition(Duration.millis(200), node);
        back.setToX(0);
        back.play();

        ScaleTransition scaleBack = new ScaleTransition(Duration.millis(200), node);
        scaleBack.setToX(1);
        scaleBack.setToY(1);
        scaleBack.play();
    }

    public Paintable getActiveBrush() {
        return activeBrush;
    }
}
