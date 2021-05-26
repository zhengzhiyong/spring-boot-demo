package com.xkcoding.fop.demofop.util.fop;

import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

public class FopResourceResolver implements ResourceResolver {
    @Override
    public Resource getResource(URI uri) throws IOException {
        if (null == uri) {
            throw new RuntimeException("uri cannot be null");
        }
        if ("classpath".equals(uri.getScheme())){
            //load resource from classpath
            URL resource = FopResourceResolver.class.getResource(uri.getPath());
            if (null == resource){
                throw new RuntimeException("Resource not found:" + uri);
            }
            return new Resource(resource.openStream());
        }
        return new Resource(uri.toURL().openStream());
    }

    @Override
    public OutputStream getOutputStream(URI uri) throws IOException {
        return new FileOutputStream(new File(uri));
    }
}
