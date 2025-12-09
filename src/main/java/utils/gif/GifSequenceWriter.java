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

    public GifSequenceWriter(ImageOutputStream out,
                             int imageType,
                             int delayMS,
                             boolean loopForever) throws IOException {

        writer = ImageIO.getImageWritersBySuffix("gif").next();
        params = writer.getDefaultWriteParam();

        ImageTypeSpecifier type = ImageTypeSpecifier.createFromBufferedImageType(imageType);
        metadata = writer.getDefaultImageMetadata(type, params);

        String metaFormat = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormat);

        // --- Graphic Control Extension ---
        IIOMetadataNode gce = new IIOMetadataNode("GraphicControlExtension");
        gce.setAttribute("disposalMethod", "none");
        gce.setAttribute("userInputFlag", "FALSE");
        gce.setAttribute("transparentColorFlag", "FALSE");
        gce.setAttribute("delayTime", Integer.toString(delayMS / 10)); // in 1/100 sec
        gce.setAttribute("transparentColorIndex", "0");
        root.appendChild(gce);

        // --- Loop forever ---
        if (loopForever) {
            IIOMetadataNode appExtensions = new IIOMetadataNode("ApplicationExtensions");
            IIOMetadataNode app = new IIOMetadataNode("ApplicationExtension");

            app.setAttribute("applicationID", "NETSCAPE");
            app.setAttribute("authenticationCode", "2.0");

            app.setUserObject(new byte[]{1, 0, 0}); // loop flag

            appExtensions.appendChild(app);
            root.appendChild(appExtensions);
        }

        metadata.setFromTree(metaFormat, root);

        writer.setOutput(out);
        writer.prepareWriteSequence(null);
    }

    public void writeFrame(BufferedImage img) throws IOException {
        writer.writeToSequence(new IIOImage(img, null, metadata), params);
    }

    public void close() throws IOException {
        writer.endWriteSequence();
    }
}
