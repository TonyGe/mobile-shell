/**
 *
 */
package com.dianping.mobile.framework.util;

import com.dianping.mobile.framework.exception.ApplicationRuntimeException;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author kewen.yao
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static final Logger log = Logger.getLogger(SpringUtil.class);
    private static ApplicationContext springContext;

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String bean) {
        if (springContext == null) {
            log.error("springContext == null");
            throw new ApplicationRuntimeException("springContext == null");
        }
        boolean isExist = springContext.containsBean(bean);
        if (isExist) {
            return (T) springContext.getBean(bean);
        } else {
            //LOG.warn("action == null,key=" + bean);
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.springContext = applicationContext;
    }

}
