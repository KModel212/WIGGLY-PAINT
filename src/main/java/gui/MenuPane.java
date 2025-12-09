package gui;

import canvas.CanvasData;
import com.sun.javafx.scene.canvas.CanvasHelper;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;


import javafx.scene.text.Font;
import utils.themes.ThemeManager;


public class MenuPane extends Pane {

    private HBox menuBar;
    private Line bottomLine;

    private final CanvasPane canvasPane;
    private final CanvasData canvasData;

    public MenuPane(CanvasPane canvasPane, CanvasData canvasData) {
        this.canvasPane = canvasPane;
        this.canvasData = canvasData;
        menuBar = new HBox(20);
        menuBar.setPadding(new Insets(5, 20, 5, 20));
        menuBar.setStyle(
                "-fx-font-family: 'PixelOperator' ;" +
                        "-fx-font-size: 14px;"
        );

        bottomLine = new Line(0, 0, 0, 0);
        bottomLine.setStroke(ThemeManager.get().fg);
        bottomLine.setStrokeWidth(1.5);



        Label umm = createButton("Umm");
        Label file = createButton("File");

        setupFileMenu(file,canvasPane); // 👈 ผูกเมนูให้ปุ่ม File

        menuBar.getChildren().addAll(umm, file);


        setBackground(new Background(new BackgroundFill(
                ThemeManager.get().bg,   // your theme background color
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        getChildren().addAll(menuBar, bottomLine);
        menuBar.setMouseTransparent(false);
        this.setMouseTransparent(false);
        this.setPickOnBounds(true);


    }

    private void setupFileMenu(Label fileLabel, CanvasPane canvasPane) {
        ContextMenu fileMenu = new ContextMenu();

        MenuItem newCanvas = new MenuItem("New Canvas");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");

        fileMenu.getItems().addAll(
                newCanvas,
                open,
                save,
                exit
        );

        // ✅ กดแล้วเมนูเด้ง
        fileLabel.setOnMouseClicked(e -> {
            fileMenu.show(fileLabel, e.getScreenX(), e.getScreenY());
        });

        newCanvas.setOnAction(e -> {
            System.out.println("NEW CANVAS ✅");

            int size = canvasPane.getCanvasSize();

            // ✅ ล้างภาพที่แสดงบนจอ
            canvasPane.layer1.getGraphicsContext2D().clearRect(0, 0, size, size);
            canvasPane.layer2.getGraphicsContext2D().clearRect(0, 0, size, size);
            canvasPane.layer3.getGraphicsContext2D().clearRect(0, 0, size, size);

            // ✅ ล้างข้อมูลจริงใน CanvasData
            canvasData.clearAll(CanvasData.BG);

            // วาดพื้นหลัง + ขอบกลับมาใหม่
            canvasPane.layer0.getGraphicsContext2D().setFill(ThemeManager.get().bg);
            canvasPane.layer0.getGraphicsContext2D().fillRect(0, 0,
                    canvasPane.getInternalSize(),
                    canvasPane.getInternalSize());

            canvasPane.layer0.getGraphicsContext2D().setStroke(ThemeManager.get().fg);
            canvasPane.layer0.getGraphicsContext2D().setLineWidth(1);
            canvasPane.layer0.getGraphicsContext2D().strokeRect(
                    0, 0,
                    canvasPane.getInternalSize(),
                    canvasPane.getInternalSize()
            );


        });

        exit.setOnAction(e -> {
            System.out.println("EXIT ✅");
            javafx.application.Platform.exit();  // ✅ ปิดโปรแกรม
        });

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
        label.setPickOnBounds(true);   // ให้คลิกได้ทั้งพื้นที่ Label

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
