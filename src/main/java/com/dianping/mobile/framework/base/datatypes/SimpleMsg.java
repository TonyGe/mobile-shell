package com.dianping.mobile.framework.base.datatypes;

import com.dianping.mobile.framework.annotation.MobileDo;
import com.dianping.mobile.framework.annotation.MobileDo.MobileField;

@MobileDo(id = 0x909d)
public class SimpleMsg {
    /**
     * client only have two state: 1 and not 1 here use ICON_ALERT to represent
     * not ICON_INFO
     */
    protected static final int ICON_INFO = 1;
    protected static final int ICON_ALERT = 0;
    /**
     * The flag is application dependent, different business module have
     * different meaning. business module can use it to transfer extra info for
     * they own purpose.
     */
    @MobileField(key = 0x73ad, name = "Flag")
    protected int flag;
    @MobileField(key = 0x36e9, name = "Title")
    private String title;
    @MobileField(key = 0x57b6, name = "Content")
    private String content;
    @MobileField(key = 0xb0bb, name = "Icon")
    private int icon = ICON_ALERT;
    /**
     * 设置为NULL标示成功
     */
    private StatusCode statusCode = StatusCode.ERROR;

    public SimpleMsg() {

    }

    /**
     * 请返回给APP自定义的Server Status Code 以便进行端到端的打点和Cat记录
     *
     * @see StatusCode
     */
    @Deprecated
    public SimpleMsg(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public SimpleMsg(String title, String content, StatusCode statusCode) {
        this.title = title;
        this.content = content;
        this.statusCode = statusCode;
    }

    @Deprecated
    public SimpleMsg(String title, String content, int flag) {
        this.title = title;
        this.content = content;
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
