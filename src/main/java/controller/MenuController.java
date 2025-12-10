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

/**
 * Controller for the top menu bar of WigglyPaint.
 * <p>
 * Handles:
 * <ul>
 *     <li>Window dragging (custom undecorated window)</li>
 *     <li>New canvas creation</li>
 *     <li>Exporting wiggle animation as GIF</li>
 *     <li>Theme switching (classic/pastel)</li>
 *     <li>Application exit</li>
 * </ul>
 *
 * The MenuController communicates with:
 * <ul>
 *     <li>{@link CanvasPane} to clear layers and redraw</li>
 *     <li>{@link CanvasData} to reset framebuffer</li>
 *     <li>{@link Stage} to allow dragging for undecorated mode</li>
 * </ul>
 */
public class MenuController {

    // ============================================================
    // Fields
    // ============================================================

    /** UI menu pane containing all interactive buttons. */
    private final MenuPane view;

    /** Canvas display containing all layered canvases. */
    private final CanvasPane canvasPane;

    /** Pixel buffers used for painting + wiggle animation. */
    private final CanvasData canvasData;

    /** Application stage (needed for window dragging). */
    private final Stage stage;


    // ============================================================
    // Constructor
    // ============================================================

    /**
     * Creates a menu controller and attaches all required event handlers.
     *
     * @param view        the menu UI pane
     * @param canvasPane  the canvas display panel
     * @param canvasData  pixel framebuffer
     * @param stage       main application window
     */
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

    /**
     * Connects all menu button actions to their handler methods:
     * <ul>
     *     <li>Window dragging</li>
     *     <li>New canvas</li>
     *     <li>Export GIF</li>
     *     <li>Theme switching</li>
     *     <li>Exit</li>
     * </ul>
     */
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

    /**
     * Enables dragging the undecorated window by clicking the menu bar.
     */
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

    /**
     * Clears all drawing layers, resets pixel data, and redraws
     * the base canvas background + border.
     */
    private void setupNewCanvas() {
        view.btnNewCanvas.setOnMouseClicked(e -> {
            System.out.println("[Menu] New Canvas");

            int size = canvasPane.getCanvasSize();
            int internal = canvasPane.getInternalSize();

            // Clear paint layers
            canvasPane.layer1.getGraphicsContext2D().clearRect(0, 0, size, size);
            canvasPane.layer2.getGraphicsContext2D().clearRect(0, 0, size, size);
            canvasPane.layer3.getGraphicsContext2D().clearRect(0, 0, size, size);

            // Reset pixel framebuffer
            canvasData.clearAll(CanvasData.BG);

            // Redraw base layer background
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

    /**
     * Opens a file chooser, collects wiggle frame A & B, converts them
     * to {@link BufferedImage}, and writes them into a looping GIF file.
     * <p>
     * The exported GIF alternates between the two wiggle frames,
     * producing the signature WigglyPaint shake animation.
     */
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
                        100,   // 100ms per frame = 10 FPS
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

    /**
     * Connects the theme menu to ThemeManager.
     * Two built-in themes:
     * <ul>
     *     <li>classic</li>
     *     <li>pastel</li>
     * </ul>
     */
    private void setupThemeMenu() {
        view.themeMenu.getItems().get(0).setOnAction(e ->
                ThemeManager.setTheme("classic")
        );
        view.themeMenu.getItems().get(1).setOnAction(e ->
                ThemeManager.setTheme("pastel")
        );
    }


    // ============================================================
    // 5. EXIT APPLICATION
    // ============================================================

    /**
     * Closes the application immediately.
     */
    private void setupExit() {
        view.btnExit.setOnMouseClicked(e -> {
            System.out.println("[Menu] Exit");
            javafx.application.Platform.exit();
        });
    }
}
