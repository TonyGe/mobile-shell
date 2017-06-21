package com.dianping.mobile.framework.base.datatypes;

import com.dianping.mobile.framework.annotation.MobileDo;

@MobileDo(id = 0x6389)
public class SuccessMsg extends SimpleMsg {

    public SuccessMsg() {
        setIcon(ICON_INFO);
        setStatusCode(HttpCode.HTTPOK);
    }

    public SuccessMsg(String title, String content) {
        super(title, content);
        setIcon(ICON_INFO);
        setStatusCode(HttpCode.HTTPOK);
    }

}
