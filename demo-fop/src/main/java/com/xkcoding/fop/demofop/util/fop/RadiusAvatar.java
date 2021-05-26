package com.xkcoding.fop.demofop.util.fop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 图像画圆形处理类
 */
@Slf4j
public class RadiusAvatar {

    private final static String PNG_TYPE = "PNG";

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    static class ImageSize {
        private double width;
        private double height;
    }


    public static String makeCircularImg(String srcFilePath, String circularImgSavePath, int targetSize, int cornerRadius) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(srcFilePath));
        BufferedImage circularBufferImage = roundImage(bufferedImage, targetSize, cornerRadius);
        ImageIO.write(circularBufferImage, PNG_TYPE, new File(circularImgSavePath));
        return circularImgSavePath;
    }

    private static BufferedImage roundImage(BufferedImage image, int targetSize, int cornerRadius) {
        BufferedImage outputImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outputImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, targetSize, targetSize, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return outputImage;
    }

    public static ImageSize getImageSize(File file) {
        ImageSize imageSize = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            imageSize = ImageSize.builder()
                .height(bufferedImage.getHeight())
                .width(bufferedImage.getWidth())
                .build();
        } catch (IOException e) {
            log.error("call method getImageSize has an error:{0}", e);
        }
        return imageSize;
    }

    public static void radiusAvatar(String picturePath) {
        try {
            ImageSize imageSize = getImageSize(new File(picturePath));
            int cornerRadius = (int) Math.min(imageSize.height, imageSize.width);
            makeCircularImg(picturePath, picturePath, cornerRadius, cornerRadius);
        } catch (IOException e) {
            log.error("call method radiusAvatar has an error:{0}", e);
        }
    }


    public static void radiusAvatar(String networkUrl, String outputFilePath) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(networkUrl));
            int cornerRadius = Math.min(bufferedImage.getHeight(), bufferedImage.getWidth());
            BufferedImage circularBufferedImage = roundImage(bufferedImage, cornerRadius, cornerRadius);
            ImageIO.write(circularBufferedImage, PNG_TYPE, new File(outputFilePath));
        } catch (IOException e) {
            log.error("call method radiusAvatar has an error:{0}", e);
        }
    }

    public static void radiusAvatar(String networkUrl, File outputFile) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(networkUrl));
            int cornerRadius = Math.min(bufferedImage.getHeight(), bufferedImage.getWidth());
            BufferedImage circularBufferedImage = roundImage(bufferedImage, cornerRadius, cornerRadius);
            ImageIO.write(circularBufferedImage, PNG_TYPE, outputFile);
        } catch (IOException e) {
            log.error("call method radiusAvatar has an error:{0}", e);
        }
    }

    public static void main(String[] args) {
        radiusAvatar("D:\\个人\\照片\\微信图片_20210515181903.jpg");
    }
}
