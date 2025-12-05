package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefWidth(600);
        borderPane.setPrefHeight(800);

        //add MenuPane to top  hbox


        //add CanvasPane to left  stackpane


        //add BrushPane to right  vbox


        //add OptionPane to bottom  hbox


        /*
        MineSweeperPane mineSweeperPane = new MineSweeperPane();
        ControlPane controlPane = new ControlPane(mineSweeperPane);
        ControlGridPane controlGridPane = new ControlGridPane(controlPane);

        GameLogic.getInstance().setControlPane(controlPane);
        hBox.getChildren().addAll(mineSweeperPane, controlGridPane);
        */

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("WigglyPaint");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

