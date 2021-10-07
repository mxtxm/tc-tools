package com.tctools.common.util;

import org.slf4j.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;


public class ImageEdit {

    private static final Logger log = LoggerFactory.getLogger(ImageEdit.class);


    public static boolean pngToJpg(String pngPath, String jpgPath) {
        Path source = Paths.get(pngPath);
        Path target = Paths.get(jpgPath);

        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(source.toFile());
        } catch (IOException e) {
            log.error("!", e);
            return false;

        }

        BufferedImage newBufferedImage = new BufferedImage(
            originalImage.getWidth(),
            originalImage.getHeight(),
            BufferedImage.TYPE_INT_RGB);

        newBufferedImage.createGraphics()
            .drawImage(originalImage,
                0,
                0,
                Color.WHITE,
                null
            );

        try {
            ImageIO.write(newBufferedImage, "jpg", target.toFile());
        } catch (IOException e) {
            log.error("!", e);
            return false;
        }
        return true;
    }

}

