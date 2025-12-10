package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import render.BackgroundRender;
import utils.config.Config;
import utils.themes.ThemeManager;

import controller.BrushController;
import controller.MenuController;
import controller.CanvasController;

import canvas.CanvasData;

import gui.BrushPane;
import gui.CanvasPane;
import gui.MenuPane;

/**
 * Main entry point for the WigglyPaint application.
 * <p>
 * Responsible for initializing:
 * <ul>
 *     <li>Root layout</li>
 *     <li>Canvas subsystem</li>
 *     <li>Theme refresh listeners</li>
 *     <li>Menu and brush UI panels</li>
 *     <li>Controllers for canvas, brushes, and menu</li>
 *     <li>Stage and window configuration (undecorated window)</li>
 * </ul>
 * No application logic is modified here; only UI structure is assembled.
 */
public class Main extends Application {

    /**
     * Initializes and displays the main JavaFX stage.
     *
     * @param primaryStage the primary stage provided by JavaFX runtime
     * @throws Exception if initialization fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // ============================================================
        // ROOT LAYOUT
        // ============================================================
        BorderPane root = new BorderPane();
        root.setPrefWidth(Config.getInt("application.default_width"));
        root.setPrefHeight(Config.getInt("application.default_height"));
        root.setBackground(BackgroundRender.dotBackgroundFromConfig());

        // refresh background when theme changes
        ThemeManager.addListener(() ->
                root.setBackground(BackgroundRender.dotBackgroundFromConfig())
        );


        // ============================================================
        // CANVAS
        // ============================================================
        CanvasPane canvasPane = new CanvasPane();
        CanvasData canvasData = new CanvasData();

        root.setCenter(canvasPane);
        BorderPane.setMargin(canvasPane, new Insets(20));


        // ============================================================
        // TOP MENU
        // ============================================================
        MenuPane menuPane = new MenuPane();
        new MenuController(menuPane, canvasPane, canvasData, primaryStage);


        // ============================================================
        // RIGHT BRUSH PANEL
        // ============================================================
        BrushPane brushPane = new BrushPane();
        BrushController brushController = new BrushController(brushPane);

        // Rebind recolored icons when theme changes
        brushPane.setOnThemeRefreshed(brushController::rebindIcons);


        // ============================================================
        // CANVAS CONTROLLER + WIGGLE LOOP
        // ============================================================
        CanvasController canvasController = new CanvasController(
                canvasPane, canvasData, brushController
        );
        canvasController.startWiggleLoop();


        // ============================================================
        // ATTACH UI PANELS
        // ============================================================
        root.setRight(brushPane);
        root.setTop(menuPane);


        // ============================================================
        // SCENE + STAGE SETTINGS
        // ============================================================
        Scene scene = new Scene(
                root,
                Config.getInt("application.default_width"),
                Config.getInt("application.default_height")
        );
        scene.setFill(Color.TRANSPARENT); // Needed for undecorated window

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("WigglyPaint");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
