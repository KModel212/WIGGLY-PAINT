package gui;

import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import utils.themes.ThemeManager;

public class MenuPane extends Pane {

    // ----------------------------
    // UI ELEMENTS
    // ----------------------------
    public final HBox menuBar = new HBox(20);

    public final Label btnUmm;
    public final Label btnNewCanvas;
    public final Label btnExportGIF;
    public final Label btnTheme;
    public final Label btnExit;

    public final ContextMenu themeMenu = new ContextMenu();

    private final Line bottomLine = new Line();
    private final Font font = Font.font("DejaVu Sans Mono", 14);

    public MenuPane() {

        // --------------------------
        // MENU BAR
        // --------------------------
        menuBar.setPadding(new Insets(5, 20, 5, 20));
        menuBar.setBackground(new Background(
                new BackgroundFill(ThemeManager.get().bg, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        // --------------------------
        // BOTTOM LINE
        // --------------------------
        bottomLine.setStroke(ThemeManager.get().fg);
        bottomLine.setStrokeWidth(1);

        // --------------------------
        // BUTTONS
        // --------------------------
        btnUmm       = createBtn("Umm");
        btnNewCanvas = createBtn("NewCanvas");
        btnExportGIF = createBtn("ExportGIF");
        btnTheme     = createBtn("Theme");

        // --------------------------
        // EXIT BUTTON (SQUARE X)
        // --------------------------
        btnExit = new Label("☒"); // square X glyph (pixel-style friendly)
        btnExit.setFont(font);
        btnExit.setTextFill(ThemeManager.get().fg);
        btnExit.setPadding(new Insets(0));
        btnExit.setPrefSize(20, 20);  // square button
        btnExit.setMinSize(20, 20);
        btnExit.setMaxSize(20, 20);
        btnExit.setStyle("-fx-alignment: center;");

        // --------------------------
        // ADD BUTTONS WITH SPACER
        // --------------------------
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  // push exit to right

        menuBar.getChildren().addAll(
                btnUmm,
                btnNewCanvas,
                btnExportGIF,
                btnTheme,
                spacer,      // <-- pushes exit to the right
                btnExit      // right side
        );

        // --------------------------
        // THEME MENU
        // --------------------------
        MenuItem itemClassic = new MenuItem("Classic");
        MenuItem itemPastel  = new MenuItem("Pastel");

        themeMenu.getItems().addAll(itemClassic, itemPastel);

        btnTheme.setOnMouseClicked(e ->
                themeMenu.show(btnTheme, e.getScreenX(), e.getScreenY())
        );

        // --------------------------
        // LAYOUT ROOT
        // --------------------------
        getChildren().addAll(menuBar, bottomLine);

        // --------------------------
        // THEME UPDATES
        // --------------------------
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

    private Label createBtn(String text) {
        Label lb = new Label(text);
        lb.setFont(font);
        lb.setTextFill(ThemeManager.get().fg);
        lb.setPadding(new Insets(2, 6, 2, 6));
        lb.setPickOnBounds(true);
        return lb;
    }

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
