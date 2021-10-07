package com.tctools.web.patch;

import org.apache.commons.imaging.*;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
//https://stackoverflow.com/questions/2744909/java-detecting-image-format-resize-scale-and-save-as-jpeg

public class PictureEdit {

    private byte[] data;


    public PictureEdit() {

    }

    public PictureEdit(String inputFilepath) throws IOException {
        read(inputFilepath);
    }

    public PictureEdit read(String inputFilepath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(inputFilepath));
        data = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return this;
    }

    public void write(String outputFilepath) throws IOException {
        IOUtils.write(data, new FileOutputStream(new File(outputFilepath)));
    }

    public PictureEdit resize(int maxSize) throws IOException, ImageReadException, ImageWriteException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
        if (image.getWidth() > maxSize || image.getHeight() > maxSize) {
            TiffImageMetadata metadata = readExifMetadata(data);
            image = Scalr.resize(image, maxSize);
            byte[] resizedData = getJpegAsBytes(image);
            data = metadata == null ? resizedData : getWithExifMetadata(metadata, resizedData);
        }
        return this;
    }

    public PictureEdit setQuality(Float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        writer.setOutput(out);
        //IIOImage outputImage = new IIOImage((data, null, null);
  //      writer.write(null, outputImage, param);
        writer.dispose();

        out.close();
        data = out.toByteArray();

        return this;
    }


    private TiffImageMetadata readExifMetadata(byte[] jpegData) throws ImageReadException, IOException {
        ImageMetadata imageMetadata = Imaging.getMetadata(jpegData);
        if (imageMetadata == null) {
            return null;
        }
        JpegImageMetadata jpegMetadata = (JpegImageMetadata) imageMetadata;
        return jpegMetadata.getExif();
    }

    private byte[] getWithExifMetadata(TiffImageMetadata metadata, byte[] jpegData)
        throws ImageReadException, ImageWriteException, IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ExifRewriter().updateExifMetadataLossless(jpegData, out, metadata.getOutputSet());
        out.close();
        return out.toByteArray();
    }

    private byte[] getJpegAsBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", out);
        out.close();
        return out.toByteArray();
    }
}
