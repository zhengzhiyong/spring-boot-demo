package com.xkcoding.fop.demofop.util.qrcode;


import cn.hutool.extra.qrcode.BufferedImageLuminanceSource;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
/**
 * @author laobiao
 * @date 2021年5月26日 10点50分
 * @desc
 */
@Slf4j
public class QRCodeUtil {

    private static final String CHARSET_UTF_8 = StandardCharsets.UTF_8.name();
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int    QRCODE_SIZE = 130;
    // LOGO宽度
    private static final int    WIDTH       = 130;
    // LOGO高度
    private static final int    HEIGHT      = 60;

    private static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET_UTF_8);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
            hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        // 插入图片
        insertImage(image, imgPath, needCompress);
        return image;
    }

    private static BufferedImage createImage(String content, InputStream inputStream, boolean needCompress,int size) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET_UTF_8);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size,
            hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (inputStream == null) {
            return image;
        }
        // 插入图片
        insertImage(image, inputStream, needCompress,size);
        return image;
    }

    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            log.info("call method insertImage the file does not exist");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        // 压缩LOGO
        if (needCompress) {
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > WIDTH) {
                height = WIDTH;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    private static void insertImage(BufferedImage source, InputStream inputStream, boolean needCompress,int size) throws Exception {
        if (null == inputStream) {
            log.info("call method insertImage the inputStream is null");
            return;
        }
        Image src = ImageIO.read(inputStream);
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        // 压缩LOGO
        if (needCompress) {
            if (width > size) {
                width = size;
            }
            if (height > size) {
                height = size;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (size - width) / 2;
        int y = (size - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, imgPath, needCompress);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    public static void encode(String content, InputStream inputStream, String destPath, boolean needCompress, int size) throws Exception {
        BufferedImage image = createImage(content, inputStream, needCompress,size);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }
    public static BufferedImage encode(String content, String imgPath, boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, imgPath, needCompress);
        return image;
    }

    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static void encode(String content, String imgPath, String destPath) throws Exception {
        encode(content, imgPath, destPath, false);
    }

    public static void encode(String content, String destPath) throws Exception {
        encode(content, null, destPath, false);
    }

    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
        throws Exception {
        BufferedImage image = createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        encode(content, null, output, false);
    }

    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET_UTF_8);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    public static String decode(String path) throws Exception {
        return decode(new File(path));
    }

    /**
     *  小程序首页二维码
     */
    private static void createIndexIcon() throws Exception {
        String diskFullPath = QRCodeUtil.class.getClassLoader().getResource("").getPath() + "res/image/indexQrcode.png";
        String indexUrl = "alipays://platformapi/startapp?appId=2019080166061464&page=pages/index/index";
        String iconUrl = "/res/image/LOGO.png";
        encode(indexUrl, QRCodeUtil.class.getResourceAsStream(iconUrl), diskFullPath, true,184);
    }

    public static void main(String[] args) throws Exception {
        createIndexIcon();
    }

}
