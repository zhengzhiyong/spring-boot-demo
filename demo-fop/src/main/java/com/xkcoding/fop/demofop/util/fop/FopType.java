package com.xkcoding.fop.demofop.util.fop;

import org.apache.fop.apps.MimeConstants;

/**
 * 输出类型，目前支持PNG和PDF
 */
public enum FopType {

    /**
     * PNG格式
     */
    PNG(MimeConstants.MIME_PNG);

    private String mimeType;

    public String getMimeType() {
        return mimeType;
    }

    private FopType(String mimeType){
        this.mimeType = mimeType;
    }

}
