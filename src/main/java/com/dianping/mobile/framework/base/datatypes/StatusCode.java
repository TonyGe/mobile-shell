package com.dianping.mobile.framework.base.datatypes;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务逻辑错误状态码，等同ERRORCODE
 * 作为{@link com.dianping.mobile.framework.base.datatypes.SimpleMsg}和{@link com.dianping.mobile.framework.datatypes.CommonMobileResponse}的状态码
 * 1）cat上会根据每个Action的状态码来分别记录统计数据
 * <p/>
 * 这里区别于HTTP STATUS CODE，shell会根据一定的规则对错误状态码和HTTPCODE做映射，一般的业务错误，都会作为200正常在端到端显示
 *
 * @author yangzhongwei
 * @date 2014-11-07 14:51
 */
public class StatusCode {

    /**
     * SimpleMsg默认出错码
     */
    public static StatusCode ERROR = new StatusCode(500);

    /**
     * MainServlet出错码
     */
    public static StatusCode FRAMEERROR = new StatusCode(100);

    /**
     * 参数错误
     */
    public static StatusCode PARAMERROR = new StatusCode(110);

    /**
     * 版本验证失败
     */
    public static StatusCode VERSIONERROR = new StatusCode(111);

    /**
     * post,get方法验证失败
     */
    public static StatusCode HTTPTYPEERROR = new StatusCode(112);

    /**
     * userage验证失败
     */
    public static StatusCode USAGEERROR = new StatusCode(113);

    /**
     * ACTION异常
     */
    public static StatusCode ACTIONEXCEPTION = new StatusCode(130);

    /**
     * Pegion调用异常
     */
    public static StatusCode PEGIONEXCEPTION = new StatusCode(510);
    static ThreadLocal<StatusCode> localcode = new ThreadLocal<StatusCode>();
    private static Map<Integer, StatusCode> codes = new HashMap<Integer, StatusCode>();
    private int code = 0;

    StatusCode(int code) {
        this.code = code;
    }

    /**
     * 新建自定义的ERRORCODE，请优先使用预定义的错误码，如果没有，请使用5XX作为服务器异常进行扩展
     *
     * @param code 错误码
     * @return 自定义的ERRORCODE
     */
    public static StatusCode code(int code) {
        StatusCode result;
        if ((result = codes.get(code)) == null)
            codes.put(code, result = new StatusCode(code));
        return result;
    }

    /**
     * 在业务逻辑的分支中临时设置业务code
     * <p/>
     * 1）优先使用ACTION返回的CommonMobileResponse状态码，如果使用了默认的SimpleMsg和CommonMobileResponse，则使用此处定义的code
     * 2）后者覆盖前者
     * 3）如果老代码修改action的返回逻辑比较复杂，可以在业务分支中临时使用，灵活处理
     */
    public static void ERROR(StatusCode code) {
        localcode.set(code);
    }

    public static void init() {
        localcode.set(null);
    }

    public int getCode() {
        return code;
    }
}
