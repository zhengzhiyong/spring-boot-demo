package com.xkcoding.fop.demofop.util.fop;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.InputStream;

/**
 * Java 字体工具
 */
@Slf4j
public class JavaFontUtil {

    //待定义
    private static String STKAITI_EN = "STKAITI";
    private static String STKAITI_LOCALE = "STKAITI";

    static {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try(InputStream fontIs = JavaFontUtil.class.getResourceAsStream("/res/fonts/STKAITI.ttf")){
            Font demoFont = Font.createFont(Font.TRUETYPE_FONT, fontIs);
            String fontName = demoFont.getFontName();
            STKAITI_LOCALE = fontName.replace("_"," ");
            ge.registerFont(demoFont);
        }catch (Exception e){
            log.error("Error in register font:/res/fonts/STKAITI.ttf" ,e);
        }
    }

    public static String getFontNameDemo(boolean isLocale){
        if (isLocale){
            return STKAITI_LOCALE;
        }
        return STKAITI_EN;
    }

    public static void init(){
        // just call $clinit
    }
}
