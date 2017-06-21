/**
 *
 */
package com.dianping.mobile.framework.action.validate;

import com.dianping.mobile.framework.annotation.MobileRequest.Param;
import com.dianping.mobile.framework.base.datatypes.SimpleMsg;
import com.dianping.mobile.framework.datatypes.CommonMobileResponse;
import com.dianping.mobile.framework.datatypes.IMobileResponse;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;

/**
 * @author kewen.yao
 */
public class ParamValidatorRequired implements ParamValidator {

    private static final SimpleMsg MSG_ERROR_INPUT_INVALID = new SimpleMsg("提示", "参数错误，int型参数值必须大于等于0");
    private static final CommonMobileResponse RESPONSE_ERROR_INPUT_INVALID = new CommonMobileResponse(MSG_ERROR_INPUT_INVALID);

    private static final SimpleMsg MSG_ERROR_LACKOFPARAM = new SimpleMsg("错误", "缺少必要参数!");
    private static final CommonMobileResponse RESPONSE_ERROR_LACKOFPARAM = new CommonMobileResponse(MSG_ERROR_LACKOFPARAM);

    /**
     * validate required param not null string not empty int must > 0 array.size must > 0
     */
    @Override
    public IMobileResponse validate(Param param, Object value) {
        if (param.required()) {
            if (value == null) {
                return RESPONSE_ERROR_LACKOFPARAM;
            } else if (value instanceof Integer) {
                if ((Integer) value < 0) {
                    return RESPONSE_ERROR_INPUT_INVALID;
                }
            } else if (value instanceof String) {
                if (StringUtils.isBlank((String) value)) {
                    return RESPONSE_ERROR_LACKOFPARAM;
                }
            } else if (value.getClass().isArray()) {
                final int length = Array.getLength(value);
                if (length == 0) {
                    return RESPONSE_ERROR_LACKOFPARAM;
                }
            }
        }
        return null;
    }

}
