package utils.gif;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GifSequenceWriter {

    private final ImageWriter writer;
    private final ImageWriteParam params;
    private final IIOMetadata metadata;


    // ============================================================
    // Constructor: sets up GIF writer, metadata, loop behavior
    // ============================================================
    public GifSequenceWriter(
            ImageOutputStream out,
            int imageType,
            int delayMS,
            boolean loopForever
    ) throws IOException {

        // Writer for type GIF
        writer = ImageIO.getImageWritersBySuffix("gif").next();
        params = writer.getDefaultWriteParam();

        ImageTypeSpecifier type =
                ImageTypeSpecifier.createFromBufferedImageType(imageType);

        metadata = writer.getDefaultImageMetadata(type, params);

        String metaFormat = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root =
                (IIOMetadataNode) metadata.getAsTree(metaFormat);


        // ------------------------------------------------------------
        // Graphic Control Extension
        // ------------------------------------------------------------
        IIOMetadataNode gce = new IIOMetadataNode("GraphicControlExtension");
        gce.setAttribute("disposalMethod", "none");
        gce.setAttribute("userInputFlag", "FALSE");
        gce.setAttribute("transparentColorFlag", "FALSE");
        gce.setAttribute("delayTime", Integer.toString(delayMS / 10)); // (1/100 sec)
        gce.setAttribute("transparentColorIndex", "0");
        root.appendChild(gce);


        // ------------------------------------------------------------
        // Loop Forever (NETSCAPE extension)
        // ------------------------------------------------------------
        if (loopForever) {
            IIOMetadataNode appExtensions = new IIOMetadataNode("ApplicationExtensions");
            IIOMetadataNode app = new IIOMetadataNode("ApplicationExtension");

            app.setAttribute("applicationID", "NETSCAPE");
            app.setAttribute("authenticationCode", "2.0");
            app.setUserObject(new byte[] { 1, 0, 0 }); // 1 = loop forever

            appExtensions.appendChild(app);
            root.appendChild(appExtensions);
        }


        // Apply metadata
        metadata.setFromTree(metaFormat, root);

        // Prepare writer
        writer.setOutput(out);
        writer.prepareWriteSequence(null);
    }


    // ============================================================
    // Write a single GIF frame
    // ============================================================
    public void writeFrame(BufferedImage img) throws IOException {
        writer.writeToSequence(new IIOImage(img, null, metadata), params);
    }


    // ============================================================
    // Close GIF sequence
    // ============================================================
    public void close() throws IOException {
        writer.endWriteSequence();
    }
}
