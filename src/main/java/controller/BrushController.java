package controller;

import brush.*;
import gui.BrushPane;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class BrushController {

    // ============================================================
    // Fields
    // ============================================================
    private final BrushPane pane;

    // Brush objects
    private final Paintable pencil      = new PencilBrush(1);
    private final Paintable fountain    = new FountainBrush(5, 0.6);
    private final Paintable marker      = new PencilBrush(3);
    private final Paintable spray       = new SprayBrush(6, 0);
    private final Paintable highlightA  = new HighlightBrush(5, 2, 0);
    private final Paintable highlightB  = new HighlightBrush(5, 3, 0);
    private final Paintable highlightC  = new HighlightBrush(5, 4, 0);
    private final Paintable eraser      = new EraserBrush(10);

    private Paintable activeBrush = null;
    private Node currentBrushNode = null;


    // ============================================================
    // Constructor
    // ============================================================
    public BrushController(BrushPane pane) {
        this.pane = pane;
        // Attach icon → brush mappings
        attach(pane.pencilIcon,     pencil);
        attach(pane.fountainIcon,   fountain);
        attach(pane.markerIcon,     marker);
        attach(pane.sprayIcon,      spray);
        attach(pane.highlightAIcon, highlightA);
        attach(pane.highlightBIcon, highlightB);
        attach(pane.highlightCIcon, highlightC);
        attach(pane.eraserIcon,     eraser);
        // Default brush
        selectBrush(pencil, pane.pencilIcon);
    }


    // ============================================================
    // Event attachment helper
    // ============================================================
    private void attach(Node icon, Paintable brush) {
        icon.setOnMouseClicked(e -> selectBrush(brush, icon));
    }


    // ============================================================
    // Brush selection logic
    // ============================================================
    private void selectBrush(Paintable brush, Node icon) {
        activeBrush = brush;
        // reset old brush visuals
        if (currentBrushNode != null) {
            animateBack(currentBrushNode);
            currentBrushNode.setEffect(null);
        }
        // animate new one
        animateSelect(icon);
        currentBrushNode = icon;
    }


    // ============================================================
    // Selection animations
    // ============================================================
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


    // ============================================================
    // Brush getter
    // ============================================================
    public Paintable getActiveBrush() {
        return activeBrush;
    }


    // ============================================================
    // Utility (brush → string name)
    // ============================================================
    private String getBrushName(Paintable b) {
        if (b == pencil) return "pencil";
        if (b == fountain) return "fountain";
        if (b == marker) return "marker";
        if (b == spray) return "spray";
        if (b == highlightA) return "highlightA";
        if (b == highlightB) return "highlightB";
        if (b == highlightC) return "highlightC";
        if (b == eraser) return "eraser";
        return "";
    }


    // ============================================================
    // Rebind icons after theme change
    // ============================================================
    public void rebindIcons() {
        // save active brush
        Paintable previous = activeBrush;
        // reset old node animation state
        if (currentBrushNode != null) {
            currentBrushNode.setTranslateX(0);
            currentBrushNode.setScaleX(1);
            currentBrushNode.setScaleY(1);
        }
        // reattach events to new ImageViews
        attach(pane.pencilIcon,     pencil);
        attach(pane.fountainIcon,   fountain);
        attach(pane.markerIcon,     marker);
        attach(pane.sprayIcon,      spray);
        attach(pane.highlightAIcon, highlightA);
        attach(pane.highlightBIcon, highlightB);
        attach(pane.highlightCIcon, highlightC);
        attach(pane.eraserIcon,     eraser);
        // restore highlight on selected brush icon
        if (previous != null) {
            String name = getBrushName(previous);
            Node newNode = pane.getIconFor(name);
            selectBrush(previous, newNode);
        }
    }
}
