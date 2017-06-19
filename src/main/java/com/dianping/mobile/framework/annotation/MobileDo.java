package com.dianping.mobile.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dianping.mobile.base.datatypes.enums.Platform;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MobileDo {

	int id() default 0;
	String name() default "";

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface MobileField {
		int key() default 0;
		String name() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface MobileVersion {
		String[] values();
		boolean negated() default false;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface MobilePlatform {
		Platform[] values();
		boolean include() default true;
	}

}