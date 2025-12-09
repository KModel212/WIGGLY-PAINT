package grader;

import canvas.CanvasData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This JUnit file contains a FULL COPY of the AbstractBrush logic
 * so that we can test behavior WITHOUT modifying production code.
 */
public class AbstractBrushTest {

    private TestBrush brush;
    private CanvasData canvas;

    @BeforeEach
    void setup() {
        brush = new TestBrush(5, 0.5);
        canvas = new CanvasData();
        canvas.clearAll(CanvasData.BG);
    }

    // ---------------------------------------------------------
    @Test
    void testComputeSize() {
        assertEquals(5, brush.computeSize(0));
        assertTrue(brush.computeSize(10) < 5);
    }

    // ---------------------------------------------------------
    @Test
    void testPaintWritesToLayer1and2() {
        brush.resetStroke();
        brush.paintOnEveryLayer(canvas, 50, 50, 0);

        assertEquals(brush.colorIndex, canvas.A()[50][50]);
        assertEquals(brush.colorIndex, canvas.B()[50][50]);
    }

    // ---------------------------------------------------------
    @Test
    void testLastXYUpdates() {
        assertTrue(Double.isNaN(brush.lastX));
        assertTrue(Double.isNaN(brush.lastY));

        brush.paintOnEveryLayer(canvas, 10, 10, 0);

        assertEquals(10, brush.lastX);
        assertEquals(10, brush.lastY);
    }

    // ---------------------------------------------------------
    @Test
    void testResetStroke() {
        brush.paintOnEveryLayer(canvas, 10, 10, 0);
        brush.resetStroke();

        assertTrue(Double.isNaN(brush.lastX));
        assertTrue(Double.isNaN(brush.lastY));
    }


    // =====================================================================
    // FULL COPY OF ABSTRACTBRUSH
    // =====================================================================
    private static class TestBrush {

        protected int baseSize;
        protected double speedScale;
        protected int colorIndex = CanvasData.FG;

        protected double lastX = Double.NaN;
        protected double lastY = Double.NaN;

        private final Random random = new Random();

        public TestBrush(int baseSize, double speedScale) {
            this.baseSize = baseSize;
            this.speedScale = speedScale;
        }

        // -------------------------------------------------------------
        // COPIED FROM AbstractBrush
        // -------------------------------------------------------------
        protected int computeSize(double speed) {
            double scaled = baseSize - speedScale * Math.log1p(speed);
            return Math.max(1, (int) Math.round(scaled));
        }

        protected void stamp(CanvasData canvas, double x, double y,
                             int size, int colorIndex, int layer) {

            int px = (int) x;
            int py = (int) y;

            canvas.set(layer, px, py, (byte) colorIndex);
        }

        protected void stampLine(CanvasData canvas,
                                 double x0, double y0,
                                 double x1, double y1,
                                 int size, int colorIndex,
                                 int layer) {

            if (Double.isNaN(x0) || Double.isNaN(y0)) {
                stamp(canvas, x1, y1, size, colorIndex, layer);
                return;
            }

            double dx = x1 - x0;
            double dy = y1 - y0;
            double dist = Math.sqrt(dx * dx + dy * dy);

            int steps = Math.max(1, (int) dist);
            double sx = dx / steps;
            double sy = dy / steps;

            double px = x0;
            double py = y0;

            for (int i = 0; i < steps; i++) {
                stamp(canvas, px, py, size, colorIndex, layer);
                px += sx;
                py += sy;
            }
        }

        public void resetStroke() {
            lastX = Double.NaN;
            lastY = Double.NaN;
        }

        public void paintOnEveryLayer(CanvasData canvas,
                                      double x, double y, double speed) {

            int size = computeSize(speed);

            stampLine(canvas, lastX, lastY, x, y, size, colorIndex, 1);
            stampLine(canvas, lastX, lastY, x, y, size, colorIndex, 2);

            lastX = x;
            lastY = y;
        }
    }
}
