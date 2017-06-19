/**
 * 
 */
package com.dianping.mobile.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.dianping.avatar.log.AvatarLogger;
import com.dianping.avatar.log.AvatarLoggerFactory;
import com.dianping.mobile.framework.action.ActionFactory;
import com.dianping.mobile.framework.exception.ApplicationRuntimeException;

/**
 * @author kewen.yao
 *
 */
@Component
public class SpringUtil implements ApplicationContextAware {

	private static final AvatarLogger LOG = AvatarLoggerFactory.getLogger(ActionFactory.class);
	private static ApplicationContext springContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.springContext = applicationContext;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String bean) {
		if(springContext == null) {
			LOG.error("springContext == null");
			throw new ApplicationRuntimeException("springContext == null");
		}
		boolean isExist = springContext.containsBean(bean);
		if(isExist) {
			return (T)springContext.getBean(bean);
		} else {
			//LOG.warn("action == null,key=" + bean);
			return null;
		}
	}

}
