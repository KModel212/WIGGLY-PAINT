package application;

import canvas.CanvasData;
import controller.CanvasController;
import gui.CanvasPane;
import gui.MenuPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
        root.setTop(new MenuPane());

        //add CanvasPane to left - stackpane 500 * 500
        CanvasPane canvasPane = new CanvasPane();
        canvasPane.setPrefWidth(500);
        canvasPane.setPrefHeight(500);
        root.setLeft(canvasPane);
        BorderPane.setMargin(canvasPane, new Insets(20));

        CanvasData data = new CanvasData();
        CanvasController controller = new CanvasController(canvasPane, data);
        controller.startWiggleLoop();

        //add BrushPane to right - vbox


        //add OptionPane to bottom - hbox


        /*
        MineSweeperPane mineSweeperPane = new MineSweeperPane();
        ControlPane controlPane = new ControlPane(mineSweeperPane);
        ControlGridPane controlGridPane = new ControlGridPane(controlPane);

        GameLogic.getInstance().setControlPane(controlPane);
        hBox.getChildren().addAll(mineSweeperPane, controlGridPane);
        */

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

