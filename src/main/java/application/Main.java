package application;

import controller.BrushController;
import gui.BrushPane;
import canvas.CanvasData;
import controller.CanvasController;
import gui.CanvasPane;
import gui.MenuPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import render.BackgroundRender;
import utils.config.Config;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Font.loadFont(getClass().getResourceAsStream("/fonts/pixel_operator/PixelOperator.ttf"), 14);

        BorderPane root = new BorderPane();
        root.setPrefWidth(Config.getInt("application.default_width"));
        root.setPrefHeight(Config.getInt("application.default_height"));
        root.setBackground(BackgroundRender.dotBackgroundFromConfig());

        //add MenuPane to top - hbox


        //add CanvasPane to left - stackpane 500 * 500
        CanvasPane canvasPane = new CanvasPane();
        canvasPane.setPrefWidth(500);
        canvasPane.setPrefHeight(500);
        root.setCenter(canvasPane);
        BorderPane.setMargin(canvasPane, new Insets(20));

        CanvasData data = new CanvasData();
        // ===============================
        // ✅ ส่ง CanvasPane เข้า MenuPane
        // ===============================
        MenuPane menuPane = new MenuPane(canvasPane, data);

        // ===============================
        // ส่วนอื่น ๆ ตามเดิม
        // ===============================
        BrushPane brushPane = new BrushPane();

        BrushController brushController = new BrushController(brushPane);
        CanvasController canvasController = new CanvasController(canvasPane, data ,brushController);

        root.setRight(brushPane);
        root.setTop(menuPane);

        canvasController.startWiggleLoop();

        Scene scene = new Scene(root ,
                Config.getInt("application.default_width") ,
                Config.getInt("application.default_height"));

        primaryStage.setScene(scene);
        primaryStage.setTitle("WigglyPaint");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

