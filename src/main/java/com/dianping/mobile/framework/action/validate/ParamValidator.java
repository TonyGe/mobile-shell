/**
 *
 */
package com.dianping.mobile.framework.action.validate;

import com.dianping.mobile.framework.annotation.MobileRequest.Param;
import com.dianping.mobile.framework.datatypes.IMobileResponse;

/**
 * @author kewen.yao
 */
public interface ParamValidator {

    IMobileResponse validate(Param param, Object value);

}
