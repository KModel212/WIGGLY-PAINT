package controller;

import brush.*;
import gui.BrushPane;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Controller responsible for managing all brush tools displayed in the
 * {@link BrushPane} and handling brush selection logic.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Initialize all brush objects</li>
 *     <li>Attach UI icons to their corresponding brushes</li>
 *     <li>Animate icon selection / deselection</li>
 *     <li>Track the currently active brush</li>
 *     <li>Rebind icons after theme changes (recoloring)</li>
 * </ul>
 */
public class BrushController {

    // ============================================================
    // Fields
    // ============================================================

    /** The pane containing all brush icons. */
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

    /** Currently selected brush tool. */
    private Paintable activeBrush = null;

    /** Node (icon) representing the current active brush. */
    private Node currentBrushNode = null;


    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Creates a controller that manages UI → brush selection behavior.
     *
     * @param pane the BrushPane that holds all brush icons
     */
    public BrushController(BrushPane pane) {
        this.pane = pane;

        // Map icons to brushes
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

    /**
     * Binds a UI icon to a specific brush tool.
     * When the icon is clicked, the brush becomes active.
     *
     * @param icon  the clickable icon node
     * @param brush the brush to activate when clicked
     */
    private void attach(Node icon, Paintable brush) {
        icon.setOnMouseClicked(e -> selectBrush(brush, icon));
    }


    // ============================================================
    // Brush selection logic
    // ============================================================

    /**
     * Marks a brush as selected, updates animations, and updates UI state.
     *
     * @param brush the newly selected brush
     * @param icon  the icon node associated with that brush
     */
    private void selectBrush(Paintable brush, Node icon) {
        activeBrush = brush;

        // Remove visual highlight from old icon
        if (currentBrushNode != null) {
            animateBack(currentBrushNode);
            currentBrushNode.setEffect(null);
        }

        // Apply selection animation to new icon
        animateSelect(icon);

        currentBrushNode = icon;
    }


    // ============================================================
    // Selection animations
    // ============================================================

    /**
     * Animates the icon when it becomes selected (slight nudge left + scale).
     *
     * @param node the icon to animate
     */
    private void animateSelect(Node node) {
        TranslateTransition out = new TranslateTransition(Duration.millis(200), node);
        out.setToX(-20);
        out.play();

        ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.play();
    }

    /**
     * Restores the icon to its default position and scale.
     *
     * @param node icon to animate back
     */
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

    /**
     * @return the currently active brush tool
     */
    public Paintable getActiveBrush() {
        return activeBrush;
    }


    // ============================================================
    // Utility (brush → string name)
    // ============================================================

    /**
     * Retrieves a string identifier for a given brush.
     * Used mainly for restoring selection after theme refresh.
     *
     * @param b the brush to identify
     * @return name string or empty if unknown
     */
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

    /**
     * Re-attaches all icon events after theme recoloring replaces ImageView nodes.
     * <p>
     * Restores:
     * <ul>
     *     <li>Click handlers</li>
     *     <li>Selection animations</li>
     *     <li>Previously active brush</li>
     * </ul>
     */
    public void rebindIcons() {
        // Preserve currently active brush
        Paintable previous = activeBrush;

        // Reset old selected icon's transform
        if (currentBrushNode != null) {
            currentBrushNode.setTranslateX(0);
            currentBrushNode.setScaleX(1);
            currentBrushNode.setScaleY(1);
        }

        // Re-attach event bindings to newly recolored icons
        attach(pane.pencilIcon,     pencil);
        attach(pane.fountainIcon,   fountain);
        attach(pane.markerIcon,     marker);
        attach(pane.sprayIcon,      spray);
        attach(pane.highlightAIcon, highlightA);
        attach(pane.highlightBIcon, highlightB);
        attach(pane.highlightCIcon, highlightC);
        attach(pane.eraserIcon,     eraser);

        // Restore visual selection on the previously active brush
        if (previous != null) {
            String name = getBrushName(previous);
            Node newNode = pane.getIconFor(name);
            selectBrush(previous, newNode);
        }
    }
}
