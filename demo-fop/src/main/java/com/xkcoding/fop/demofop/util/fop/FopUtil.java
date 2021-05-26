package com.xkcoding.fop.demofop.util.fop;

import lombok.extern.slf4j.Slf4j;
import org.apache.fop.apps.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;

/**
 * @author laobiao
 * @date 2021年5月26日 10点50分
 * @desc fop工具类
 */
@Slf4j
public class FopUtil {

    /**
     * 初始化字体
     */
    static {
        JavaFontUtil.init();
    }

    private static final VelocityProcessTool VELOCITY_PROCESS_TOOL = VelocityProcessTool.INSTANCE;

    public static VelocityEngine velocityEngine = null;

    /**
     * 支付宝头像地址前缀
     */
    private static final String ALIPAY_URL_PREFIX = "https://tfs.alipayobjects.com";


    /**
     * 执行一个 fo 的渲染，当前可以渲染成 PNG, PDF 文件
     */
    public static void convertToFile(FopFactory fopFactory, FopType type, InputStream in, OutputStream out) throws IOException {
        try (InputStreamReader reader=new InputStreamReader(in)){
            long startMillis = Instant.now().toEpochMilli();
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            foUserAgent.setConserveMemoryPolicy(true);
            Fop fop = fopFactory.newFop(type.getMimeType(), foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Source src = new StreamSource(reader);
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
            long endMillis = Instant.now().toEpochMilli();
            log.info("method convertToFile cost time:{0}", String.valueOf(endMillis - startMillis));
        } catch (Exception e) {
            log.error("method convertToFile errorMsg:{0}", e);
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                out.flush();
            }
        }
    }

    /**
     * 对字符做escape转换， 转换字母 `&`, `<` , `>`
     */
    public static String escapeCharForXML(char c) {
        if (c == '&') {
            return "&amp;";
        } else if (c == '<') {
            return "&lt;";
        } else if (c == '>') {
            return "&gt;";
        } else {
            return new String(new char[]{c});
        }
    }


    /**
     * VelocityEngine 是线程安全的，所以全局维护一个实例
     */
    public synchronized static VelocityEngine getVelocityEngine() {
        if (velocityEngine == null) {
            try {
                Properties properties = new Properties();
                properties.setProperty("input.encoding", StandardCharsets.UTF_8.name());
                properties.setProperty("output.encoding", StandardCharsets.UTF_8.name());
                properties.setProperty("Content-Type", "text/xml;charset=UTF-8");
                properties.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName());
                velocityEngine = new VelocityEngine(properties);
            } catch (Exception e) {
                log.error("{0}",e,e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return velocityEngine;
    }

    /**
     * 解析Velocity模版并返回模板内容
     */
    public static String mergeTemplateWithVelocity(String templateName, Map<String, Object> context) {
        VelocityContext vc = new VelocityContext();
        if (context != null) {
            context.entrySet().forEach(entry -> {
                String k = entry.getKey();
                Object v = entry.getValue();
                if ((v != null) && (v instanceof String) && (k != null)) {
                    vc.put(k, escapeForXML((String) v));
                } else {
                    vc.put(k, v);
                }
            });
        }
        if (context != null) {
            context.put("processTool", VELOCITY_PROCESS_TOOL);
            // 在PDF中使用字体的英文名，但在PNG（AWT）中使用系统Locale的名字
            context.put("font_use_locale_name", true);
            context.put("fop_type", FopType.PNG.name().toLowerCase());
        }

        VelocityEngine ve = getVelocityEngine();
        StringWriter sw = new StringWriter();
        try {
            ve.mergeTemplate(templateName, StandardCharsets.UTF_8.name(), vc, sw);
            return sw.toString();
        } catch (ResourceNotFoundException e) {
            log.error("{0}",e,e.getMessage());
            throw new RuntimeException(e);
        } catch (ParseErrorException e) {
            log.error("{0}",e,e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("{0}",e,e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 对字符做escape转换， 转换字母 `&`, `<` , `>`
     */
    public static String escapeForXML(String str) {
        StringBuffer sb = new StringBuffer((str == null) ? 0 : str.length());
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                sb.append(escapeCharForXML(c));
            }
        }
        return sb.toString();
    }

    public static FopFactory createFopFactory(String resourceName) {
        try {
            URL url = FopUtil.class.getResource(resourceName);
            try (InputStream is = url.openStream()) {
                return new FopConfParser(is, url.toURI(), new FopResourceResolver())
                    .getFopFactoryBuilder()
                    .build();
            }
        } catch (Exception e) {
            log.error("{0}",e,e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public static FopFactory createFopFactory() {
        try {
            return FopFactory.newInstance(new File(".").toURI());
        } catch (Exception e) {
            log.error("{0}",e,e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String replaceEscapeCharAtStr(String content) {
        content = content.replaceAll("&amp;", "&").replaceAll("&lt;", "<")
            .replaceAll("&gt;", ">");
        while (content.contains("&amp;") || content.contains("&lt;") || content.contains("&gt;")) {
            content = replaceEscapeCharAtStr(content);
        }
        return content;
    }

    /**
     * 执行一个 fo 的渲染，当前可以渲染成 PNG, PDF 文件，输入为Velocity Template Resource Name
     */
    public static void convertFoTemplateVelocity(FopFactory fopFactory, FopType type, String templateName,
                                                 Map<String, Object> context, OutputStream out)
        throws IOException, FOPException {
        if (context != null) {
            context.put("processTool", VELOCITY_PROCESS_TOOL);
            // 在PDF中使用字体的英文名，但在PNG（AWT）中使用系统Locale的名字
            if (type == FopType.PNG) {
                context.put("font_use_locale_name", true);
            } else {
                context.put("font_use_locale_name", false);
            }
            // 用于分别在PDF和PNG中加载图片使用，PDF支持矢量图，PNG对矢量图像素化效果差
            context.put("fop_type", type.name().toLowerCase());
        }

        String foContent = mergeTemplateWithVelocity(templateName, context);
        try (InputStream foIs = new ByteArrayInputStream(foContent.getBytes(StandardCharsets.UTF_8))) {
            convertToFile(fopFactory, type, foIs, out);
        } catch (Exception e) {
            log.error( "Render fo to {0} erorr",e,type.name());
            throw e;
        }
    }

}
