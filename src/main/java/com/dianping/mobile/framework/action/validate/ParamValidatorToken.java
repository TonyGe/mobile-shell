/**
 * 
 */
package com.dianping.mobile.framework.action.validate;

import org.apache.commons.lang.StringUtils;

import com.dianping.mobile.framework.annotation.MobileRequest.Param;
import com.dianping.mobile.framework.base.datatypes.SimpleMsg;
import com.dianping.mobile.framework.datatypes.CommonMobileResponse;
import com.dianping.mobile.framework.datatypes.IMobileResponse;
import com.dianping.mobile.framework.util.TokenUtil;

/**
 * @author kewen.yao
 *
 */
public class ParamValidatorToken implements ParamValidator {
	
	private static final SimpleMsg MSG_ERROR_TOKEN_INVALID = new SimpleMsg("提示", "token 非法");
	private static final CommonMobileResponse RESPONSE_ERROR_TOKEN_INVALID = new CommonMobileResponse(MSG_ERROR_TOKEN_INVALID);
	
	private static final String PARAM_TOKEN = "token";
	
	/**
	 * validate token legally
	 */
	@Override
	public IMobileResponse validate(Param param, Object value) {
		if(PARAM_TOKEN.equalsIgnoreCase(param.name())) {
			if(!StringUtils.isBlank((String) value)) {
				int tokenUserId = TokenUtil.parseToken((String) value);
				if(tokenUserId <= 0 && param.required()) {
					return RESPONSE_ERROR_TOKEN_INVALID;
				}
			}
		}
		return null;
	}

}
