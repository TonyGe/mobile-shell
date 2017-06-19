package com.dianping.mobile.framework.action;

import com.dianping.avatar.log.AvatarLogger;
import com.dianping.avatar.log.AvatarLoggerFactory;
import com.dianping.mobile.base.datatypes.bean.ClientInfoRule;
import com.dianping.mobile.framework.action.validate.ParamValidator;
import com.dianping.mobile.framework.action.validate.ParamValidatorRequired;
import com.dianping.mobile.framework.action.validate.ParamValidatorToken;
import com.dianping.mobile.framework.action.validate.ParamValidatorUserInput;
import com.dianping.mobile.framework.annotation.Action;
import com.dianping.mobile.framework.annotation.MobileRequest;
import com.dianping.mobile.framework.annotation.MobileRequest.Param;
import com.dianping.mobile.framework.base.datatypes.SimpleMsg;
import com.dianping.mobile.framework.base.datatypes.StatusCode;
import com.dianping.mobile.framework.datatypes.CommonMobileResponse;
import com.dianping.mobile.framework.datatypes.IMobileContext;
import com.dianping.mobile.framework.datatypes.IMobileRequest;
import com.dianping.mobile.framework.datatypes.IMobileResponse;
import com.dianping.mobile.framework.exception.ApplicationRuntimeException;
import com.dianping.mobile.framework.io.ResponseType;
import com.dianping.mobile.framework.normalize.DefaultRequestNormalizer;
import com.dianping.mobile.framework.normalize.IRequestNormalizer;
import com.dianping.mobile.framework.util.ClientRuleUtil;
import com.dianping.mobile.framework.util.TokenUtil;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class DefaultAction<REQUEST extends IMobileRequest> implements
		IAction {

	
	//private static final SimpleMsg MSG_ERROR_BLOCKUSER = new SimpleMsg("错误","对不起，您没有权限进行此操作");

	//private static final SuccessMsg SUCCESS_MSG_ERROR_BLOCKUSER = new SuccessMsg("错误", "对不起，您没有权限进行此操作");

	private static final String PARAM_TOKEN = "token";
	
	private static final SimpleMsg MSG_ERROR_VERSION_CLIENT = new SimpleMsg("提示", "该版本的客户端 不允许方法该接口", StatusCode.VERSIONERROR);
	
	private static final CommonMobileResponse RESPONSE_ERROR_VERSION_CLIENT = new CommonMobileResponse(MSG_ERROR_VERSION_CLIENT);

	

	//private static final CommonMobileResponse RESPONSE_ERROR_BLOCKUSER = new CommonMobileResponse(MSG_ERROR_BLOCKUSER);

	//private static final CommonMobileResponse RESPONSE_SUCCESS_BLOCKUSER = new CommonMobileResponse(SUCCESS_MSG_ERROR_BLOCKUSER);

	

//	protected final AvatarLogger log = AvatarLoggerFactory.getLogger(this.getClass());

	private final String className = getClass().getName();

    
	@Override
    public IMobileResponse execute(IMobileContext context) {
        List<ClientInfoRule> list = getRule();
        if(!ClientInfoRule.validateOr(list, context.getClient(), context.getVersion())) {
            return RESPONSE_ERROR_VERSION_CLIENT;
        }
        
		Action action = this.getClass().getAnnotation(Action.class);
        if(!ClientInfoRule.validateOr(ClientRuleUtil.getClientInfoRules(action), context.getClient(), context.getVersion())) {
            return RESPONSE_ERROR_VERSION_CLIENT;
        }
		
		Class<REQUEST> clazz = getGenericType();
		REQUEST request = normalize(context, clazz);
		IMobileResponse preValidateResp = preValidate(request, clazz);
		if (preValidateResp != null) {
			return preValidateResp;
		}
		IMobileResponse validateResp = validate(request, context);
		if (validateResp != null) {
			return validateResp;
		}
		
		//if token is required, replace userid in context with id parsed from token
		int userId = getTokenUserId(request, clazz);
		if(userId > 0){
			context.setUserId(userId);
		}
		return execute(request, context);
	}

	private int getTokenUserId(REQUEST request, Class<REQUEST> clazz){
		int tokenUserId = 0;
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			Param param = field.getAnnotation(Param.class);
			if(param != null) {
				if(PARAM_TOKEN.equalsIgnoreCase(param.name())) {
					field.setAccessible(true);
					try {
						Object value = field.get(request);
						if(value != null && value instanceof String) {
							if(!StringUtils.isBlank((String)value)) {
								tokenUserId = TokenUtil.parseToken((String)value);
							}
						}
					} catch (IllegalArgumentException e) {
						throw new ApplicationRuntimeException("This should not happen", e);
					} catch (IllegalAccessException e) {
						throw new ApplicationRuntimeException("This should not happen", e);
					}
					break;
				}
			}	
		}
		
		return tokenUserId;
	}
	
	@Override
	public String getActionKey() {
		@SuppressWarnings("unchecked")
		Class<? extends DefaultAction> clazz = this.getClass();
		Action annotation = clazz.getAnnotation(Action.class);
		if (annotation != null) {
			return annotation.url();
		}
		return StringUtils.EMPTY;
	}
	
	@Override
	public String getHttpType() {
		@SuppressWarnings("unchecked")
		Class<? extends DefaultAction> clazz = this.getClass();
		Action annotation = clazz.getAnnotation(Action.class);
		if (annotation != null) {
			return annotation.httpType();
		}
		return StringUtils.EMPTY;
	}

	@Override
	public boolean isCheckToken() {
		@SuppressWarnings("unchecked")
		Class<? extends DefaultAction> clazz = this.getClass();
		Action annotation = clazz.getAnnotation(Action.class);
		if (annotation != null) {
			return annotation.isCheckToken();
		}
		return false;
	}

	@Override
	public ResponseType getEncryption() {
		@SuppressWarnings("unchecked")
		Class<? extends DefaultAction> clazz = this.getClass();
		Action annotation = clazz.getAnnotation(Action.class);
		if (annotation != null) {
			return annotation.encryption();
		}
		return ResponseType.MAPI;
	}

	/* (non-Javadoc)
             * @see com.dianping.mobile.framework.action.IAction#postCompressed()
             */
	@Override
	public boolean postCompressed() {
		@SuppressWarnings("unchecked")
		Class<? extends DefaultAction> clazz = this.getClass();
		Action annotation = clazz.getAnnotation(Action.class);
		if (annotation != null) {
			return annotation.postCompressed();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private Class<REQUEST> getGenericType() {
		ParameterizedType type = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class<REQUEST>) type.getActualTypeArguments()[0];
	}

	private REQUEST normalize(IMobileContext context, Class<REQUEST> clazz) {
		return getRequestNormalizer().normalize(context, clazz);
	}
	
	protected IRequestNormalizer<REQUEST> getRequestNormalizer() {
		return new DefaultRequestNormalizer<REQUEST>();
	}

	private IMobileResponse preValidate(REQUEST request, Class<REQUEST> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			IMobileResponse result = preValidate(request, field);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	private static final List<ParamValidator> LIST_PARAMVALIDATOR = new ArrayList<ParamValidator>();
	static {
		LIST_PARAMVALIDATOR.add(new ParamValidatorRequired());
		LIST_PARAMVALIDATOR.add(new ParamValidatorToken());
		LIST_PARAMVALIDATOR.add(new ParamValidatorUserInput());
	}
	
	private IMobileResponse preValidate(Object object, Field field) {
		Param param = field.getAnnotation(Param.class);
		if (param != null) {
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(object);
				if(value != null) {
					if (value.getClass().isArray()) {
						final int length = Array.getLength(value);
						if (length > 0) {
							for (int i = 0; i < length; ++i) {
								Object subObj = Array.get(value, i);
								for (Field subField: subObj.getClass().getDeclaredFields()) {
									IMobileResponse subResult = preValidate(subObj, subField);
									if (subResult != null) {
										return subResult;
									}
								}
							}
						}
					} else if (value.getClass().isAnnotationPresent(MobileRequest.class)) {
						for (Field subField: value.getClass().getDeclaredFields()) {
							IMobileResponse subResult = preValidate(value, subField);
							if (subResult != null) {
								return subResult;
							}
						}
					}
				}
				for(ParamValidator pv : LIST_PARAMVALIDATOR) {
					IMobileResponse result = pv.validate(param, value);
					if(result != null) {
						if(value instanceof String && result == ParamValidatorUserInput.RESPONSE_ERROR_INVALID_INPUT) {
							field.set(object, replaceSurrogateCharacter((String) value));
						} else {
							return result;
						}
					}
				}
			} catch (IllegalArgumentException e) {
				throw new ApplicationRuntimeException("This should not happen", e);
			} catch (IllegalAccessException e) {
				throw new ApplicationRuntimeException("This should not happen", e);
			}
		}
		return null;
	}
	
	private static boolean isSurrogate(char c) {
        return Character.isHighSurrogate(c) || Character.isLowSurrogate(c);
    }
	
	private static String replaceSurrogateCharacter(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (isSurrogate(c)) {
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }
	
	protected final String getMessageTitle() {
		return className;
	}

	protected abstract IMobileResponse validate(REQUEST request,
			IMobileContext context);

	protected abstract IMobileResponse execute(REQUEST request,
			IMobileContext context);
	
	protected abstract List<ClientInfoRule> getRule();
	
}
