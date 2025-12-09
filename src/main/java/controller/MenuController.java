package controller;

import canvas.CanvasData;
import gui.CanvasPane;
import gui.MenuPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.gif.GifSequenceWriter;
import utils.themes.ThemeManager;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;

public class MenuController {

    // ============================================================
    // Fields
    // ============================================================
    private final MenuPane view;
    private final CanvasPane canvasPane;
    private final CanvasData canvasData;
    private final Stage stage;


    // ============================================================
    // Constructor
    // ============================================================
    public MenuController(MenuPane view, CanvasPane canvasPane, CanvasData canvasData, Stage stage) {
        this.view = view;
        this.canvasPane = canvasPane;
        this.canvasData = canvasData;
        this.stage = stage;

        attachHandlers();
    }


    // ============================================================
    // Attach Event Handlers
    // ============================================================
    private void attachHandlers() {
        setupDragWindow();
        setupNewCanvas();
        setupExportGIF();
        setupThemeMenu();
        setupExit();
    }


    // ============================================================
    // 1. WINDOW DRAGGING
    // ============================================================
    private void setupDragWindow() {

        final double[] offsetX = new double[1];
        final double[] offsetY = new double[1];

        view.menuBar.setOnMousePressed(e -> {
            offsetX[0] = e.getSceneX();
            offsetY[0] = e.getSceneY();
        });

        view.menuBar.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - offsetX[0]);
            stage.setY(e.getScreenY() - offsetY[0]);
        });
    }


    // ============================================================
    // 2. NEW CANVAS
    // ============================================================
    private void setupNewCanvas() {

        view.btnNewCanvas.setOnMouseClicked(e -> {

            System.out.println("[Menu] New Canvas");

            int size = canvasPane.getCanvasSize();
            int internal = canvasPane.getInternalSize();

            // Clear drawing layers
            canvasPane.layer1.getGraphicsContext2D().clearRect(0, 0, size, size);
            canvasPane.layer2.getGraphicsContext2D().clearRect(0, 0, size, size);
            canvasPane.layer3.getGraphicsContext2D().clearRect(0, 0, size, size);

            // Reset pixel data
            canvasData.clearAll(CanvasData.BG);

            // Draw base layer background & border
            var gc = canvasPane.layer0.getGraphicsContext2D();
            gc.setFill(ThemeManager.get().bg);
            gc.fillRect(0, 0, internal, internal);

            gc.setStroke(ThemeManager.get().fg);
            gc.setLineWidth(1);
            gc.strokeRect(0, 0, internal, internal);
        });
    }


    // ============================================================
    // 3. EXPORT GIF
    // ============================================================
    private void setupExportGIF() {

        view.btnExportGIF.setOnMouseClicked(e -> {

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Export Wiggle GIF");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("GIF Files", "*.gif")
            );

            File file = chooser.showSaveDialog(view.getScene().getWindow());
            if (file == null) return;

            try (ImageOutputStream output = ImageIO.createImageOutputStream(file)) {

                GifSequenceWriter writer = new GifSequenceWriter(
                        output,
                        BufferedImage.TYPE_INT_ARGB,
                        100,   // delay per frame (100ms = 10FPS)
                        true   // loop forever
                );

                // Frame A
                BufferedImage frameA = canvasData.toBufferedImage(canvasData.A());
                writer.writeFrame(frameA);

                // Frame B
                BufferedImage frameB = canvasData.toBufferedImage(canvasData.B());
                writer.writeFrame(frameB);

                writer.close();
                System.out.println("[Export] GIF saved OK!");

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("[Export] Error saving GIF!");
            }
        });
    }


    // ============================================================
    // 4. THEME SWITCHING
    // ============================================================
    private void setupThemeMenu() {

        // Classic
        view.themeMenu.getItems().get(0).setOnAction(e ->
                ThemeManager.setTheme("classic")
        );

        // Pastel
        view.themeMenu.getItems().get(1).setOnAction(e ->
                ThemeManager.setTheme("pastel")
        );
    }


    // ============================================================
    // 5. EXIT
    // ============================================================
    private void setupExit() {
        view.btnExit.setOnMouseClicked(e -> {
            System.out.println("[Menu] Exit");
            javafx.application.Platform.exit();
        });
    }
}
