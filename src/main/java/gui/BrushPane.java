package gui;

import brush.*;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class BrushPane extends VBox {

    // =========================
    // ✅ ICON
    // =========================
    ImageView PencilBrushI = new ImageView(
            new Image(getClass().getResourceAsStream("/brush/pencil.PNG"))
    );

    ImageView FountainBrushI = new ImageView(
            new Image(getClass().getResourceAsStream("/brush/fountain.PNG"))
    );

    ImageView HighlightPenAI = new ImageView(
            new Image(getClass().getResourceAsStream("/brush/highlightA.PNG"))
    );

    ImageView HighlightPenBI = new ImageView(
            new Image(getClass().getResourceAsStream("/brush/highlightB.PNG"))
    );

    ImageView HighlightPenCI = new ImageView(
            new Image(getClass().getResourceAsStream("/brush/highlightC.PNG"))
    );

    ImageView MarkerPenI = new ImageView(
            new Image(getClass().getResourceAsStream("/brush/marker.PNG"))
    );

    ImageView SprayPenI = new ImageView(
            new Image(getClass().getResourceAsStream("/brush/spray.PNG"))
    );

    ImageView EraserPenI = new ImageView(
            new Image(getClass().getResourceAsStream("/brush/eraser.PNG"))
    );

    // =========================
    // ✅ BRUSH OBJECT
    // =========================
//    private Brushable pencil   = new PencilBrush();
//    private Brushable fountain = new FountainBrush(50,5);
//    private Brushable highlightA = new HighlightPen(50,5);
//    private Brushable highlightB = new HighlightPen(50,5);
//    private Brushable highlightC = new HighlightPen(50,5);
    private Brushable marker = new MarkerPen(50,5);
//    private Brushable spray  = new SprayPen(50,5);
//    private Brushable eraser = new EraserPen(50,5);

    // =========================
    // ✅ ACTIVE BRUSH
    // =========================
    private Brushable activeBrush;
    private Node currentBrushNode = null;

    public BrushPane() {
        super(12);
        this.setAlignment(Pos.CENTER_RIGHT);

        // ✅ ตั้งขนาดทุก ICON
        setupBrushIcon(PencilBrushI);
        setupBrushIcon(FountainBrushI);
        setupBrushIcon(HighlightPenAI);
        setupBrushIcon(HighlightPenBI);
        setupBrushIcon(HighlightPenCI);
        setupBrushIcon(MarkerPenI);
        setupBrushIcon(SprayPenI);
        setupBrushIcon(EraserPenI);

        // ✅ ผูกคลิก + เด้ง
        PencilBrushI.setOnMouseClicked(e -> selectBrush(pencil, PencilBrushI));
        FountainBrushI.setOnMouseClicked(e -> selectBrush(fountain, FountainBrushI));
        HighlightPenAI.setOnMouseClicked(e -> selectBrush(highlightA, HighlightPenAI));
        HighlightPenBI.setOnMouseClicked(e -> selectBrush(highlightB, HighlightPenBI));
        HighlightPenCI.setOnMouseClicked(e -> selectBrush(highlightC, HighlightPenCI));
        MarkerPenI.setOnMouseClicked(e -> selectBrush(marker, MarkerPenI));
        SprayPenI.setOnMouseClicked(e -> selectBrush(spray, SprayPenI));
        EraserPenI.setOnMouseClicked(e -> selectBrush(eraser, EraserPenI));

        // ✅ ใส่เข้า VBox
        this.getChildren().addAll(
                PencilBrushI,
                FountainBrushI,
                MarkerPenI,
                SprayPenI,
                HighlightPenAI,
                HighlightPenBI,
                HighlightPenCI,
                EraserPenI
        );
    }

    // =========================
    // ✅ ตั้งค่าขนาด ICON
    // =========================
    private void setupBrushIcon(ImageView icon) {
        icon.setFitWidth(200);
        icon.setPreserveRatio(true);
    }

    // =========================
    // ✅ คลิกเลือก + เด้งออก
    // =========================
    private void selectBrush(Brushable brush, Node brushNode) {

        activeBrush = brush;

        // หุบอันเก่า
        if (currentBrushNode != null) {
            TranslateTransition back =
                    new TranslateTransition(Duration.millis(200), currentBrushNode);
            back.setToX(0);
            back.play();

            ScaleTransition scaleBack =
                    new ScaleTransition(Duration.millis(200), currentBrushNode);
            scaleBack.setToX(1);
            scaleBack.setToY(1);
            scaleBack.play();

            currentBrushNode.setEffect(null);
        }

        // เด้งอันใหม่ออก (-20 เพราะอยู่ด้านขวา)
        TranslateTransition out =
                new TranslateTransition(Duration.millis(200), brushNode);
        out.setToX(-20);
        out.play();

        ScaleTransition scale =
                new ScaleTransition(Duration.millis(200), brushNode);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.play();

        brushNode.setEffect(new DropShadow());

        currentBrushNode = brushNode;
    }

    // =========================
    // ✅ ส่ง activeBrush ให้ Canvas ใช้
    // =========================
    public Brushable getActiveBrush() {
        return activeBrush;
    }
}
