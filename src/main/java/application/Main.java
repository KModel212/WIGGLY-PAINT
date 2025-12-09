package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
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


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {


        BorderPane root = new BorderPane();
        root.setPrefWidth(Config.getInt("application.default_width"));
        root.setPrefHeight(Config.getInt("application.default_height"));
        root.setBackground(BackgroundRender.dotBackgroundFromConfig());
        ThemeManager.addListener(() -> {
            root.setBackground(BackgroundRender.dotBackgroundFromConfig());
        });

        CanvasPane canvasPane = new CanvasPane();
        CanvasData canvasData = new CanvasData();
        root.setCenter(canvasPane);
        BorderPane.setMargin(canvasPane, new Insets(20));



        MenuPane menuPane = new MenuPane();
        new MenuController(menuPane, canvasPane, canvasData, primaryStage);


        BrushPane brushPane = new BrushPane();
        BrushController brushController = new BrushController(brushPane);
        brushPane.setOnThemeRefreshed(brushController::rebindIcons);

        CanvasController canvasController = new CanvasController(canvasPane, canvasData ,brushController);
        canvasController.startWiggleLoop();

        root.setRight(brushPane);
        root.setTop(menuPane);


        Scene scene = new Scene(
                root,
                Config.getInt("application.default_width"),
                Config.getInt("application.default_height")
        );
        scene.setFill(Color.TRANSPARENT);   // IMPORTANT ★

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("WigglyPaint");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

