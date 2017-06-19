package com.dianping.mobile.framework.normalize;

import java.lang.reflect.Field;

import com.dianping.mobile.framework.annotation.MobileRequest;
import com.dianping.mobile.framework.annotation.MobileRequest.Param;
import com.dianping.mobile.framework.datatypes.IMobileContext;
import com.dianping.mobile.framework.exception.ApplicationRuntimeException;

import com.dianping.mobile.framework.exception.ClientErrorException;
import com.dianping.mobile.framework.normalize.util.ReflectionUtil;

public class DefaultRequestNormalizer<T> implements IRequestNormalizer<T>{

	public T normalize(IMobileContext context, Class<T> clazz) {
		if (clazz != null && isMobileRequest(clazz)) {
			Field[] fields = clazz.getDeclaredFields();
			try {
				T request = (T) clazz.newInstance();
				for (int i = 0; i < fields.length; ++i) {
					Field field = fields[i];
					Param param = field.getAnnotation(Param.class);
					if (param != null) {
						String name = param.name();
						String value = context.getParameter(name);
						if (value != null) {
                            Object realValue = null;
                            try {
                                realValue = ReflectionUtil.parseValue(field.getType(), value);
                            } catch (ClientErrorException e) {
                                throw new ClientErrorException("parse field " + name + " error, " + e.getMessage(), e);
                            }
							if (realValue != null) {
								field.setAccessible(true);
								field.set(request, realValue);
							}
						}
					}
				}
				return request;
			} catch (InstantiationException e) {
				throw new ApplicationRuntimeException(
						"Failed to create instance of class :"
								+ clazz.getName(), e);
			} catch (IllegalAccessException e) {
				throw new ApplicationRuntimeException(
						"Default constructor is not accessible for class : "
								+ clazz.getName(), e);
			}
		} else {
			throw new ApplicationRuntimeException(
					"Class should annotate with @MobileRequest");
		}
	}

	private boolean isMobileRequest(Class<T> clazz) {
		return clazz.isAnnotationPresent(MobileRequest.class);
	}
}