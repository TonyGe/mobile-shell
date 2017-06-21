/**
 *
 */
package com.dianping.mobile.framework.io;

import com.dianping.mobile.framework.base.datatypes.HttpCode;

/**
 * @author kewen.yao
 */
public class ResponseContent {

    public static final String ENCODING = "utf-8";
    public static final String CONTENTTYPE_BINARY = "application/binary; charset=" + ENCODING;
    public static final String CONTENTTYPE_TEXT = "application/text; charset=" + ENCODING;
    public static final String CONTENTTYPE_XML = "application/xml; charset=" + ENCODING;
    public static final int STATUS_ERROR = 400;
    public static final int STATUS_SUCCESS = HttpCode.HTTP_STATUS_OK;
    private int statusCode;
    private String contentType;
    private byte[] content;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.setContent(content, 0, content.length);
    }

    public void setContent(byte[] content, int offset, int length) {
        this.content = new byte[length];
        System.arraycopy(content, offset, this.content, 0, length);
    }
}
