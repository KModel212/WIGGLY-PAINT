package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import utils.themes.ThemeManager;

import static javafx.scene.text.FontWeight.BOLD;

/**
 * The top menu bar of WigglyPaint.
 * <p>
 * Provides interactive controls for:
 * <ul>
 *     <li>New canvas creation</li>
 *     <li>GIF export</li>
 *     <li>Theme switching</li>
 *     <li>Window dragging (handled by MenuController)</li>
 *     <li>Exit button</li>
 * </ul>
 *
 * The MenuPane is a lightweight GUI container that exposes all menu
 * UI elements to {@link controller.MenuController}, which attaches
 * the actual event handlers for each button.
 *
 * <p>Additionally, it listens for theme changes and recolors itself
 * (background, text, divider line) automatically.
 */
public class MenuPane extends Pane {

    // ============================================================
    // UI ELEMENTS
    // ============================================================

    /** Horizontal container for all menu items and the exit button. */
    public final HBox menuBar = new HBox(20);

    /** Left-side button group. */
    public final Label btnUmm;
    public final Label btnNewCanvas;
    public final Label btnExportGIF;
    public final Label btnTheme;

    /** Exit button ("exit!!"). */
    public final Label btnExit;

    /** Context menu for selecting themes (classic / pastel). */
    public final ContextMenu themeMenu = new ContextMenu();

    /** Bottom divider line under the menu bar. */
    private final Line bottomLine = new Line();

    /** Font used for all menu labels. */
    private final Font font = Font.font("Comic Sans MS", BOLD, 14);


    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Builds the menu bar layout, creates all buttons,
     * sets up the theme menu, and registers theme listeners.
     */
    public MenuPane() {

        // ------------------------------------------------------------
        // Setup menu bar background + padding
        // ------------------------------------------------------------
        menuBar.setPadding(new Insets(5, 20, 5, 20));
        menuBar.setBackground(new Background(
                new BackgroundFill(ThemeManager.get().bg, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        // ------------------------------------------------------------
        // Bottom divider
        // ------------------------------------------------------------
        bottomLine.setStroke(ThemeManager.get().fg);
        bottomLine.setStrokeWidth(1);

        // ------------------------------------------------------------
        // Create labeled menu buttons
        // ------------------------------------------------------------
        btnUmm       = createBtn("Umm");
        btnNewCanvas = createBtn("NewCanvas");
        btnExportGIF = createBtn("ExportGIF");
        btnTheme     = createBtn("Theme");

        // ------------------------------------------------------------
        // Exit button ("exit!!")
        // ------------------------------------------------------------
        btnExit = new Label("exit!!");
        btnExit.setFont(font);
        btnExit.setTextFill(ThemeManager.get().fg);
        btnExit.setPadding(new Insets(0));
        btnExit.setAlignment(Pos.CENTER);

        // ------------------------------------------------------------
        // Place items in HBox with a spacer
        // ------------------------------------------------------------
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        menuBar.getChildren().addAll(
                btnUmm,
                btnNewCanvas,
                btnExportGIF,
                btnTheme,
                spacer,
                btnExit
        );

        // ------------------------------------------------------------
        // Theme selection popup menu
        // ------------------------------------------------------------
        MenuItem itemClassic = new MenuItem("Classic");
        MenuItem itemPastel  = new MenuItem("Pastel");

        themeMenu.getItems().addAll(itemClassic, itemPastel);

        // Open context menu on click
        btnTheme.setOnMouseClicked(e ->
                themeMenu.show(btnTheme, e.getScreenX(), e.getScreenY())
        );

        // ------------------------------------------------------------
        // Add menu bar + divider to root
        // ------------------------------------------------------------
        getChildren().addAll(menuBar, bottomLine);

        // ------------------------------------------------------------
        // Theme updates
        // Recolor the menu bar and all text when theme changes.
        // ------------------------------------------------------------
        ThemeManager.addListener(() -> {

            menuBar.setBackground(new Background(
                    new BackgroundFill(ThemeManager.get().bg, CornerRadii.EMPTY, Insets.EMPTY)
            ));

            btnUmm.setTextFill(ThemeManager.get().fg);
            btnNewCanvas.setTextFill(ThemeManager.get().fg);
            btnExportGIF.setTextFill(ThemeManager.get().fg);
            btnTheme.setTextFill(ThemeManager.get().fg);
            btnExit.setTextFill(ThemeManager.get().fg);

            bottomLine.setStroke(ThemeManager.get().fg);
        });
    }


    // ============================================================
    // Helper: Create a styled menu button
    // ============================================================

    /**
     * Creates a themed button with preset font, padding, and text color.
     *
     * @param text button label text
     * @return styled Label ready to be placed in the menu bar
     */
    private Label createBtn(String text) {
        Label lb = new Label(text);
        lb.setFont(font);
        lb.setTextFill(ThemeManager.get().fg);
        lb.setPadding(new Insets(2, 6, 2, 6));
        lb.setPickOnBounds(true);
        return lb;
    }


    // ============================================================
    // Layout
    // ============================================================

    /**
     * Positions the menu bar at the top and draws a divider line beneath it.
     * This method is called by the JavaFX layout engine.
     */
    @Override
    protected void layoutChildren() {

        double w = getWidth();
        double barHeight = menuBar.prefHeight(-1);

        menuBar.resizeRelocate(0, 0, w, barHeight);

        bottomLine.setStartX(0);
        bottomLine.setEndX(w);
        bottomLine.setStartY(barHeight);
        bottomLine.setEndY(barHeight);
    }
}
