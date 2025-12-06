package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import render.BackgroundRender;
import utils.config.Config;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane root = new BorderPane();
        root.setPrefWidth(Config.getInt("application.default_width"));
        root.setPrefHeight(Config.getInt("application.default_height"));
        root.setBackground(BackgroundRender.dotBackgroundFromConfig());

        //add MenuPane to top - hbox


        //add CanvasPane to left - stackpane 400 * 400


        //add BrushPane to right - vbox


        //add OptionPane to bottom - hbox


        /*
        MineSweeperPane mineSweeperPane = new MineSweeperPane();
        ControlPane controlPane = new ControlPane(mineSweeperPane);
        ControlGridPane controlGridPane = new ControlGridPane(controlPane);

        GameLogic.getInstance().setControlPane(controlPane);
        hBox.getChildren().addAll(mineSweeperPane, controlGridPane);
        */

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("WigglyPaint");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

