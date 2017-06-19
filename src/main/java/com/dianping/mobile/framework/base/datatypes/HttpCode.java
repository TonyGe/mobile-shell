package com.dianping.mobile.framework.base.datatypes;

import com.dianping.mobile.framework.datatypes.IMobileResponse;
import org.apache.http.HttpStatus;

/**
 * 预定义的HTTP状态码
 *
 * @author yangzhongwei
 * @date 2014-11-03 17:16
 */
public class HttpCode {

    /**
     * 正常返回
     */
    public static final int HTTP_STATUS_OK = 200;
    /**
     * 服务出现异常
     */
    public static final int HTTP_SERVER_ERROR = 450;
    /**
     * 用户需要重新登录
     */
    public static final int HTTP_ILLEGAL_NEWTOKEN_KEY = 401;
    /**
     * 解码错误
     */
    public static final int HTTP_INVALID_ENCRYPT_KEY = 409;

    /***************特殊的code,供APP进行交互动作****************/

    /**
     * 正常请求
     */
    public static final StatusCode HTTPOK = new StatusCode(HTTP_STATUS_OK);

    /**
     * 需要APP重新登陆
     */
    public static final StatusCode NEWTOKEN = new StatusCode(HTTP_ILLEGAL_NEWTOKEN_KEY);

    /**
     * 解码错误
     */
    public static final StatusCode ENCRYPT = new StatusCode(HTTP_INVALID_ENCRYPT_KEY);

    /**
     * 根据相应的错误码决定http的状态码
     * 目前只要应用的ACTION返回，代表服务正常200
     *
     * @return HTTP STATUS CODE
     */
    public static int httpcode(StatusCode statusCode) {
//        int servercode = servercode(response);
//        if (servercode / 100 == 2) {
//            return HTTP_STATUS_OK;
//        } else {
//            return HTTP_ERROR_MSG;
//        }
        if (statusCode == NEWTOKEN || statusCode == ENCRYPT || statusCode == HTTPOK)
            return statusCode.getCode();
        return HTTP_SERVER_ERROR;
    }

    /**
     * 获取业务状态码
     *
     * @param response
     * @return
     */
    public static StatusCode servercode(IMobileResponse response) {
        StatusCode statusCode = response.getStatusCode();
        // 默认成功和默认失败
        if ((statusCode == HttpCode.HTTPOK || statusCode == StatusCode.ERROR) && StatusCode.localcode.get() != null)
            statusCode = StatusCode.localcode.get();
        return statusCode;
    }

}
