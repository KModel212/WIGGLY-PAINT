package controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public class CanvasController {
    public CanvasController() {
        Canvas layer0 = new Canvas(400,400);
        Canvas layer1 = new Canvas(400,400);
        Canvas layer2 = new Canvas(400,400);

        StackPane layerPane = new StackPane(
                layer0,
                layer1,
                layer2
        );
        layerPane.setPrefHeight(400);
        layerPane.setPrefWidth(400);


    }
}
