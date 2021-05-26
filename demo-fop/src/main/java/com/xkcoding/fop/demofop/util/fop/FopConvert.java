package com.xkcoding.fop.demofop.util.fop;

import com.xkcoding.fop.demofop.util.qrcode.QRCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FopFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.Instant;
import java.util.*;

/**
 * @author laobiao
 * @date 2021年5月26日 10点50分
 * @desc fop工具类
 */
@Slf4j
public class FopConvert {

    private static final String RESOURCES_PATH = FopConvert.class.getClassLoader().getResource("").getPath();

    /**
     * 瀑布流目录
     */
    private static final String DEMO_DIR = "res/fop/fo/demo/";

    /**
     * fop配置
     */
    private static final String XCONF_PATH = "/res/fop/fo/fop.xconf";


    private static String STKAITI_EN = "STKAITI";

    private static final String PNG = ".PNG";

    private static final String ATTACHMENT_VIDEO_TEXT = "支付宝搜索“链上公益”，进入小程序查看。";

    private static final String PBL_TEMP_DIR = "res/fop/fo/pbl/image/temp/";

    private synchronized static String getPblFileName() {
        return RESOURCES_PATH + DEMO_DIR + UUID.randomUUID().toString() + Instant.now().toEpochMilli();
    }

    private synchronized static String getPblQRCodeFileName() {
        return PBL_TEMP_DIR + UUID.randomUUID().toString() + Instant.now().toEpochMilli() + PNG;
    }

    /**
     * 处理纯数字英文不自动换行问题
     * @param content
     * @return
     */
    private static String autoNewlineHandle(String content) {
        if (StringUtils.isNotBlank(content)) {
            char[] chars = content.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                sb.append(chars[i]).append("&#x200b;");
            }
            return sb.toString();
        }
        return null;
    }

    public static void main(String[] args) {
        yuanlao();
    }
    /**
     * 这里是一个demo
     */
    public static void yuanlao() {
        String templateName = "/res/fop/fo/demo/yuanlao.fo";
        String tempString = FopUtil.mergeTemplateWithVelocity(templateName, null);
        try {
            //图片文件输出位置
            File fopPictureFile = new File("D:\\yuanlao.png");
            if (!fopPictureFile.exists()){
                fopPictureFile.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(fopPictureFile);
            FopFactory fopFactory = FopUtil.createFopFactory(XCONF_PATH);
            FopUtil.convertFoTemplateVelocity(fopFactory, FopType.PNG, templateName, null, outputStream);
        } catch (IOException e) {
            log.error("method demo create qrcode has an error :{0}",e, e.getMessage());
        } catch (FOPException e) {
            log.error("method demo create qrcode has an error :{0}", e,e.getMessage());
        }
    }
    /**
     * 这里是一个demo
     */
    public static void demo() {
        String templateName = "/res/fop/fo/demo/demo.fo";
        Map<String, Object> context = new HashMap<>();
        context.put("name", "老表");
        String tempString = FopUtil.mergeTemplateWithVelocity(templateName, context);
        log.info(tempString);

        try {
            //图片文件输出位置
            File fopPictureFile = new File("D:\\waterfall.png");
            if (!fopPictureFile.exists()){
                fopPictureFile.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(fopPictureFile);
            FopFactory fopFactory = FopUtil.createFopFactory(XCONF_PATH);
            FopUtil.convertFoTemplateVelocity(fopFactory, FopType.PNG, templateName, context, outputStream);
        } catch (IOException e) {
            log.error("method demo create qrcode has an error :{0}",e, e.getMessage());
        } catch (FOPException e) {
            log.error("method demo create qrcode has an error :{0}", e,e.getMessage());
        }
    }

    /**
     * 处理图片蒙层问题
     * @param networkUrl
     * @param attachType 类型（0图片、1视频、2语音、3文件）
     * @return
     * @throws IOException
     */
    private synchronized static String reCreatePicture(String networkUrl,short attachType) throws Exception {
        BufferedImage fopTempFileImage = ImageIO.read(new URL(networkUrl));
        BufferedImage newBufferedImage = new BufferedImage(fopTempFileImage.getWidth(), fopTempFileImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        newBufferedImage.createGraphics().drawImage(fopTempFileImage, 0, 0, Color.WHITE, null);
        String tempFileName = getPblFileName() + PNG;
        File folder = new File(RESOURCES_PATH + DEMO_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File tempFile = new File(tempFileName);
        if (!tempFile.exists()){
            tempFile.createNewFile();
        }
        ImageIO.write(newBufferedImage,"PNG",tempFile);

        //合并两个图片(视频首帧图和播放按钮合成)
        File outFile = null,outTempFile = null,outTempFile2 = null;
        String outFileName = getPblFileName() + PNG;

        if ((short)1 == attachType){
            String outTempFileName = getPblFileName() + PNG;
            outTempFile = new File(outTempFileName);
            if (!outTempFile.exists()){
                outTempFile.createNewFile();
            }
            BufferedImage bi1 = ImageIO.read(new FileInputStream(tempFile));
            BufferedImage bi2 = ImageIO.read(FopConvert.class.getResourceAsStream("/res/image/play.png"));
            Graphics g = bi1.getGraphics();
            g.drawImage(bi2, (bi1.getWidth() - bi2.getWidth())/2 , (bi1.getHeight()
                - bi2.getHeight())/2 , null);
            g.dispose();

            OutputStream out = new FileOutputStream(outTempFileName);
            ImageIO.write(bi1, "png", out);
            out.close();

            //遮罩层处理
            String outTempFileName2 = getPblFileName() + PNG;
            outTempFile2 = new File(outTempFileName2);
            if (!outTempFile2.exists()){
                outTempFile2.createNewFile();
            }
            graphics2DCover(outTempFileName,outTempFileName2);

            //底图
            BufferedImage background = ImageIO.read(new File(outTempFileName2));
            Graphics2D bgG2 = (Graphics2D)background.getGraphics();
            bgG2.drawImage(background,0,0,Color.WHITE,null);
            //字体、字型、字号
            bgG2.setFont(new Font(STKAITI_EN, Font.PLAIN, 25));
            //画文字
            bgG2.drawString(ATTACHMENT_VIDEO_TEXT, (background.getWidth()/2 -240), background.getHeight()/2 + 60);
            bgG2.dispose();

            //图片保存到本地
            File file =new File(outFileName);
            ImageIO.write(background, "png", file);
        }
        if (tempFile != null && tempFile.exists()){
            tempFile.delete();
        }
        if (outFile != null && outFile.exists()){
            outFile.delete();
        }
        if (outTempFile != null && outTempFile.exists()){
            outTempFile.delete();
        }
        if (outTempFile2 != null && outTempFile2.exists()){
            outTempFile2.delete();
        }
        return networkUrl;
    }


    private static File graphics2DCover(String inputStreamUrl, String outputStreamUrl) throws Exception{
        //底图
        BufferedImage background = ImageIO.read(new File(inputStreamUrl));
        Graphics2D bgG2 = (Graphics2D)background.getGraphics();

        //遮罩层大小
        int coverWidth = background.getWidth();
        int coverHeight = background.getHeight();
        //创建黑色遮罩层
        BufferedImage cover = new BufferedImage(coverWidth, coverHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D coverG2 = (Graphics2D)cover.getGraphics();
        coverG2.setColor(new Color(51,51,51));
        coverG2.fillRect(0,0, coverWidth, coverHeight);
        coverG2.dispose();

        //开启透明度
        bgG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
        //描绘
        bgG2.drawImage(cover, 0, 0, coverWidth, coverHeight, null);
        //结束透明度
        bgG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        bgG2.dispose();

        //图片保存到本地
        File file =new File(outputStreamUrl);
        ImageIO.write(background, "png", file);
        return file;
    }

    /**
     * 支付宝头像处理
     * @param personnelAvatarPath
     * @return
     * @throws IOException
     */
    private synchronized static String reRadiusAvatar(String personnelAvatarPath) throws IOException{
        String outPutFilePath = getPblFileName() + PNG;
        File folder = new File(RESOURCES_PATH + DEMO_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File outPutFile = new File(outPutFilePath);
        if (!outPutFile.exists()){
            outPutFile.createNewFile();
        }
        RadiusAvatar.radiusAvatar(personnelAvatarPath,outPutFile);
        return personnelAvatarPath;
    }

    /**
     * 瀑布流对应页面二维码生成
     * @return
     */
    private synchronized static String pblQrCode(Long id,Long waterfallId,Short publicCommentType){
        try {
            //小程序前台跳转地址
            String text;
            if (null != id){
                //从瀑布流跳转
                text = "alipays://platformapi/startapp?appId=2019080166061464&page=/pages/middlePage/middlePage?isLive%3d1%26id%3d"+id;
            }else {
                //从详情页面跳转
                text = "alipays://platformapi/startapp?appId=2019080166061464&page=/pages/middlePage/middlePage?isLive%3d1%26id%3d"+waterfallId+"%26type%3d"+publicCommentType;
            }
            //小程序地址生成临时图片存放地址
            String diskPath = getPblQRCodeFileName();
            String diskFullPath = RESOURCES_PATH + diskPath;
            String iconUrl = "/res/image/LOGO.png";
            QRCodeUtil.encode(text, FopConvert.class.getResourceAsStream(iconUrl), diskFullPath, true,200);
            return iconUrl;
        } catch (Exception e) {
            log.error("method pblQrCode create qrcode has an error :{0}", e,e.getMessage());
            return null;
        }
    }

}
