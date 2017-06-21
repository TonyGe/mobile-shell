package com.dianping.mobile.framework.action;

import com.dianping.mobile.framework.exception.ApplicationRuntimeException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class ActionFactory implements ApplicationContextAware {

    //	private static final AvatarLogger LOG = AvatarLoggerFactory.getLogger(ActionFactory.class);
    private static ApplicationContext springContext;

    public static ApplicationContext getApplicationContext() {
        return springContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ActionFactory.springContext = applicationContext;
    }

    public static IAction createAction(String actionKey) {
        if (springContext == null) {
//			LOG.error("springContext == null");
            throw new ApplicationRuntimeException("springContext == null");
        }
        boolean isExsit = springContext.containsBean(actionKey);
        // 对于xx/xx.bin 或者xx/xx/xx.bin这种slb的路径，直接找action名称
        int i = actionKey.lastIndexOf('/');
        if (!isExsit && i > -1 && i < actionKey.length() - 1) {
            actionKey = actionKey.substring(i + 1);
            isExsit = springContext.containsBean(actionKey);
        }
        if (isExsit) {
            return (IAction) springContext.getBean(actionKey);
        } else {
//			LOG.warn("action == null,key=" + actionKey);
            return null;
        }
    }
}
