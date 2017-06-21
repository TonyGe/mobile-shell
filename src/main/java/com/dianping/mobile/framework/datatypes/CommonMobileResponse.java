package com.dianping.mobile.framework.datatypes;

import com.dianping.mobile.framework.base.datatypes.HttpCode;
import com.dianping.mobile.framework.base.datatypes.SimpleMsg;
import com.dianping.mobile.framework.base.datatypes.StatusCode;

public class CommonMobileResponse implements IMobileResponse {

    private Object data;
    private StatusCode statusCode = HttpCode.HTTPOK;

    public CommonMobileResponse(Object data) {
        this.data = data;
        if (data instanceof SimpleMsg && ((SimpleMsg) data).getStatusCode() != null) {
            this.statusCode = ((SimpleMsg) data).getStatusCode();
        }
    }


    public CommonMobileResponse(Object data, StatusCode statusCode) {
        this(data);
        if (statusCode != null)
            this.statusCode = statusCode;
    }

    /**
     * 使用了自定义了statuscode的SimpleMsg响应
     * {@link com.dianping.mobile.framework.base.datatypes.HttpCode}
     *
     * @param title      标题
     * @param content    内容
     * @param statusCode 自定义的服务状态码，用于Cat的数据记录
     */
    public static CommonMobileResponse simpleMsg(String title, String content, StatusCode statusCode) {
        return new CommonMobileResponse(new SimpleMsg(title, content), statusCode);
    }

    @Override
    public Object getData() {
        return this.data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

}
