package com.xkcoding.fop.demofop.util.fop;

import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.text.NumberFormat;

@Slf4j
public class VelocityProcessTool {
    private VelocityProcessTool() {
    }

    public static final VelocityProcessTool INSTANCE = new VelocityProcessTool();

    public String getDemoFont(boolean isLocale) {
        return JavaFontUtil.getFontNameDemo(isLocale);
    }

    public String printUriEncoded(String url) {
        StringBuilder sb = new StringBuilder();
        if (null != url) {
            try {
                for (int i = 0; i < url.length(); i++) {
                    char c = url.charAt(i);
                    if (isCharEncodeInEncodeUri(c)) {
                        sb.append(URLEncoder.encode(new String(new char[]{c}), "UTF-8"));
                    } else {
                        if (c == '&') {
                            sb.append("&amp;");
                        } else {
                            sb.append(c);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("call method printUriEncoded has an error :{0}", e);
                throw new RuntimeException("Should not happen:" + e.getMessage());
            }
        }
        return sb.toString();
    }

    private boolean isCharEncodeInEncodeUri(char c) {
        if (c >= 'a' && c <= 'z') {
            return false;
        }
        if (c >= 'A' && c <= 'Z') {
            return false;
        }
        if (c >= '0' && c <= '9') {
            return false;
        }

        char[] noEscapeChars = new char[]{
            ';', ',', '/', '?', ':', '@', '&', '=', '+', '$', '-', '_', '.', '!', '~', '*', '\'', '(', ')', '#'
        };
        for (int i = 0; i < noEscapeChars.length; i++) {
            if (noEscapeChars[i] == c) {
                return false;
            }
        }
        return false;
    }

    /**
     * 输出 &#8203; (零宽空格字符)分割的字符串
     *
     * @param str
     * @return
     */
    public String printZeroWidthForUrl(String str) {
        StringBuilder sb = new StringBuilder();
        if ((null != str) && (str.length() > 9)) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                sb.append(FopUtil.escapeCharForXML(c));
                if ((i > 8) && (c == '/' || c == '.' || c == '&' || c == '=' || c == '?')) {
                    sb.append("&#8203");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 输出 &#8203; (零宽空格字符)分割的字符串
     *
     * @param str
     * @return
     */
    public String printZeroWidth(String str) {
        StringBuilder sb = new StringBuilder();
        if (null != str) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (i > 0) {
                    sb.append("&#8203");
                }
                if (c == '&') {
                    //判断是否Entity
                    sb.append(c);
                    do {
                        i++;
                        sb.append(str.charAt(i));
                    } while ((str.charAt(i) != ';') && ((i + 1) < str.length()));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 输出 &#8203; (零宽空格字符)分割的字符串
     *
     * @param str
     * @return
     */
    public String printZeroWidthEscapeForXML(String str) {
        StringBuilder sb = new StringBuilder();
        if (null != str) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (i > 0) {
                    sb.append("&#8203");
                }
                sb.append(FopUtil.escapeCharForXML(c));
            }
        }
        return sb.toString();
    }


    /**
     * 输出 以下风格Fop内容：
     * <pre>
     *     你<fo:leader leader-pattern="space" />好:
     * </pre>
     */
    public String printFopAequilate(String title) {
        StringBuilder sb = new StringBuilder();
        if (null != title) {
            for (int i = 0; i < title.length(); i++) {
                char c = title.charAt(i);
                if ((i > 0) && (c != ':' && c != '：')) {
                    sb.append("<fo:leader leader-pattern=\"space\" />");
                }
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 输出字符格式
     * @param size
     * @return
     */
    public String printSize(Object size) {
        if (null == size) {
            return "";
        }
        if (size.getClass() == long.class || size.getClass() == Long.class) {
            return NumberFormat.getNumberInstance().format((long) size);
        }
        if (size.getClass() == int.class || size.getClass() == Integer.class) {
            return NumberFormat.getNumberInstance().format((int) size);
        }
        return size.toString();
    }
}
