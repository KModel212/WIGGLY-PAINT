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

public class MenuPane extends Pane {

    // ============================================================
    // UI ELEMENTS
    // ============================================================
    public final HBox menuBar = new HBox(20);

    public final Label btnUmm;
    public final Label btnNewCanvas;
    public final Label btnExportGIF;
    public final Label btnTheme;
    public final Label btnExit;

    public final ContextMenu themeMenu = new ContextMenu();

    private final Line bottomLine = new Line();
    private final Font font = Font.font("Comic Sans MS", BOLD, 14);


    // ============================================================
    // Constructor
    // ============================================================
    public MenuPane() {

        // ------------------------------------------------------------
        // Menu Bar
        // ------------------------------------------------------------
        menuBar.setPadding(new Insets(5, 20, 5, 20));
        menuBar.setBackground(new Background(
                new BackgroundFill(ThemeManager.get().bg, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        // ------------------------------------------------------------
        // Bottom Divider Line
        // ------------------------------------------------------------
        bottomLine.setStroke(ThemeManager.get().fg);
        bottomLine.setStrokeWidth(1);

        // ------------------------------------------------------------
        // Buttons
        // ------------------------------------------------------------
        btnUmm       = createBtn("Umm");
        btnNewCanvas = createBtn("NewCanvas");
        btnExportGIF = createBtn("ExportGIF");
        btnTheme     = createBtn("Theme");

        // ------------------------------------------------------------
        // Exit Button (square X)
        // ------------------------------------------------------------
        btnExit = new Label("exit!!");
        btnExit.setFont(font);
        btnExit.setTextFill(ThemeManager.get().fg);
        btnExit.setPadding(new Insets(0));
        btnExit.setAlignment(Pos.CENTER);

        // ------------------------------------------------------------
        // Layout + Spacer
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
        // Theme Menu (ContextMenu)
        // ------------------------------------------------------------
        MenuItem itemClassic = new MenuItem("Classic");
        MenuItem itemPastel  = new MenuItem("Pastel");

        themeMenu.getItems().addAll(itemClassic, itemPastel);

        btnTheme.setOnMouseClicked(e ->
                themeMenu.show(btnTheme, e.getScreenX(), e.getScreenY())
        );

        // ------------------------------------------------------------
        // Add to root
        // ------------------------------------------------------------
        getChildren().addAll(menuBar, bottomLine);

        // ------------------------------------------------------------
        // Theme Updates
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
    // Helper: Create Menu Button
    // ============================================================
    private Label createBtn(String text) {
        Label lb = new Label(text);
        lb.setFont(font);
        lb.setTextFill(ThemeManager.get().fg);
        lb.setPadding(new Insets(2, 6, 2, 6));
        lb.setPickOnBounds(true);
        return lb;
    }


    // ============================================================
    // Layout Children
    // ============================================================
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
