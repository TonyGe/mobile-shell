package com.dianping.mobile.framework.annotation;

import com.dianping.mobile.core.enums.Platform;
import com.dianping.mobile.core.enums.Product;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zhongkai.zhao
 * date: 13-9-16
 * time: 下午1:00
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface MobileClientRule {

    /**
     * 适用的Platforms
     *
     * @return
     */
    Platform[] platforms();

    /**
     * 适用的Products
     *
     * @return
     */
    Product[] products() default {Product.API};

    String minVersion() default StringUtils.EMPTY;

    String maxVersion() default StringUtils.EMPTY;
}
