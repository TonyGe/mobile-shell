package com.dianping.mobile.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zhongkai.zhao
 * date: 13-9-13
 * time: 下午4:25
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface MobileClientRules {

    MobileClientRule[] value();
}
