package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import javafx.scene.text.Font;
import utils.themes.ThemeManager;


public class MenuPane extends Pane {

    private HBox menuBar;
    private Line bottomLine;

    public MenuPane() {

        menuBar = new HBox(20);
        menuBar.setPadding(new Insets(5, 20, 5, 20));
        menuBar.setStyle(
                "-fx-font-family: 'PixelOperator' ;" +
                        "-fx-font-size: 14px;"
        );

        menuBar.getChildren().addAll(
                createButton("Umm"),
                createButton("File")
        );

        bottomLine = new Line(0, 0, 0, 0);
        bottomLine.setStroke(ThemeManager.get().fg);
        bottomLine.setStrokeWidth(1.5);

        getChildren().addAll(menuBar, bottomLine);

        setBackground(new Background(new BackgroundFill(
                ThemeManager.get().bg,   // your theme background color
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
    }

    // ---- CUSTOM LAYOUT ---- //
    @Override
    protected void layoutChildren() {

        // HBox spans full width
        menuBar.resizeRelocate(
                0,
                0,
                getWidth(),
                menuBar.prefHeight(-1)
        );

        // bottom line positions right under the HBox
        bottomLine.setStartX(0);
        bottomLine.setEndX(getWidth());
        bottomLine.setStartY(menuBar.getHeight());
        bottomLine.setEndY(menuBar.getHeight());
    }

    private Label createButton(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-text-fill: " + toHex(ThemeManager.get().fg) + ";" +
                        "-fx-font-weight: bold;"
        );
        return label;
    }

    private String toHex(Color c) {
        return String.format("#%02X%02X%02X",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255)
        );
    }
}
