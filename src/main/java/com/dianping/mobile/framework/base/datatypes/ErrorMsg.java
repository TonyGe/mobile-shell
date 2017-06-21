/**
 *
 */
package com.dianping.mobile.framework.base.datatypes;

import com.dianping.mobile.framework.annotation.MobileDo;

/**
 * @author kewen.yao
 */
@MobileDo(id = 0x909d)
public class ErrorMsg extends SimpleMsg {

    public ErrorMsg(String title, String content) {
        super(title, content);
    }

    /**
     * 使用了自定义了statuscode的SimpleMsg
     *
     * @param title   标题
     * @param content 内容
     */
    public ErrorMsg(String title, String content, StatusCode statusCode) {
        super(title, content);
        this.setStatusCode(statusCode);
    }
}
