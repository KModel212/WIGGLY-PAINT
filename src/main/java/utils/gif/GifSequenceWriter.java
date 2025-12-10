package utils.gif;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Utility class for writing animated GIFs frame-by-frame.
 * <p>
 * This class wraps the standard Java ImageIO GIF writer so that the
 * caller can:
 * <ul>
 *     <li>Specify frame delay (in milliseconds)</li>
 *     <li>Enable infinite looping (NETSCAPE extension)</li>
 *     <li>Write multiple frames sequentially</li>
 *     <li>Close the sequence when finished</li>
 * </ul>
 *
 * <p>The writer must be used as follows:
 * <pre>
 * ImageOutputStream out = ImageIO.createImageOutputStream(file);
 * GifSequenceWriter writer = new GifSequenceWriter(out,
 *                                                  BufferedImage.TYPE_INT_ARGB,
 *                                                  100,   // delay per frame
 *                                                  true); // loop forever
 *
 * writer.writeFrame(frame1);
 * writer.writeFrame(frame2);
 * ...
 * writer.close();
 * </pre>
 *
 * <p>This class is used by WigglyPaint to export its two wiggle frames
 * as a looping animation.
 */
public class GifSequenceWriter {

    /** The underlying ImageIO writer for GIFs. */
    private final ImageWriter writer;

    /** Write parameters applied to each frame. */
    private final ImageWriteParam params;

    /** Metadata describing animation delay + loop behavior. */
    private final IIOMetadata metadata;


    // ============================================================
    // Constructor — sets up GIF writer, metadata, and looping
    // ============================================================

    /**
     * Creates a GIF sequence writer that can receive frames one at a time.
     *
     * @param out          stream for writing GIF data
     * @param imageType    type of buffered image being written (e.g. TYPE_INT_ARGB)
     * @param delayMS      delay between frames, in milliseconds
     * @param loopForever  if true, inserts a NETSCAPE loop extension
     *
     * @throws IOException if writer setup fails
     */
    public GifSequenceWriter(
            ImageOutputStream out,
            int imageType,
            int delayMS,
            boolean loopForever
    ) throws IOException {

        // Obtain GIF writer instance
        writer = ImageIO.getImageWritersBySuffix("gif").next();
        params = writer.getDefaultWriteParam();

        ImageTypeSpecifier type =
                ImageTypeSpecifier.createFromBufferedImageType(imageType);

        metadata = writer.getDefaultImageMetadata(type, params);

        String metaFormat = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root =
                (IIOMetadataNode) metadata.getAsTree(metaFormat);


        // ------------------------------------------------------------
        // Graphic Control Extension (frame delay, transparency options)
        // ------------------------------------------------------------
        IIOMetadataNode gce = new IIOMetadataNode("GraphicControlExtension");
        gce.setAttribute("disposalMethod", "none");
        gce.setAttribute("userInputFlag", "FALSE");
        gce.setAttribute("transparentColorFlag", "FALSE");

        // Delay is stored in hundredths of seconds
        gce.setAttribute("delayTime", Integer.toString(delayMS / 10));
        gce.setAttribute("transparentColorIndex", "0");

        root.appendChild(gce);


        // ------------------------------------------------------------
        // Loop Forever (NETSCAPE application extension)
        // ------------------------------------------------------------
        if (loopForever) {
            IIOMetadataNode appExtensions = new IIOMetadataNode("ApplicationExtensions");
            IIOMetadataNode app = new IIOMetadataNode("ApplicationExtension");

            app.setAttribute("applicationID", "NETSCAPE");
            app.setAttribute("authenticationCode", "2.0");

            // User object bytes: { loopFlag, LSB, MSB }
            // 1 = loop forever
            app.setUserObject(new byte[] { 1, 0, 0 });

            appExtensions.appendChild(app);
            root.appendChild(appExtensions);
        }

        // Write modified metadata back
        metadata.setFromTree(metaFormat, root);

        // Prepare writer for receiving frames
        writer.setOutput(out);
        writer.prepareWriteSequence(null);
    }


    // ============================================================
    // Write a single GIF frame
    // ============================================================

    /**
     * Writes one frame into the GIF sequence using the metadata configured
     * in the constructor (delay + loop behavior).
     *
     * @param img frame image to write
     * @throws IOException if writing fails
     */
    public void writeFrame(BufferedImage img) throws IOException {
        writer.writeToSequence(new IIOImage(img, null, metadata), params);
    }


    // ============================================================
    // Close GIF sequence
    // ============================================================

    /**
     * Completes the GIF file and closes the underlying writer.
     * Must be called when all frames have been written.
     *
     * @throws IOException if finalizing the GIF fails
     */
    public void close() throws IOException {
        writer.endWriteSequence();
    }
}
