package com.dianping.mobile.framework.annotation;

import com.dianping.mobile.framework.io.ResponseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Action {

	String url();
	
	String httpType() default "";
	
	boolean postCompressed() default false;

	/**
	 * 是否对用户的Token进行验证
	 */
	boolean isCheckToken() default false;

	/**
	 * 对于结果的处理方式
	 */
	ResponseType encryption() default ResponseType.MAPI;

	MobileClientRule[] mobileClientRule() default {};

}