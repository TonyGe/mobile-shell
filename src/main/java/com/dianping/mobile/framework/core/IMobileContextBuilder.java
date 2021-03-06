package com.dianping.mobile.framework.core;

import com.dianping.mobile.framework.datatypes.MobileContext;
import com.dianping.mobile.framework.exception.ApplicationDecryptException;
import com.dianping.mobile.framework.exception.IllegalNewTokenException;
import com.dianping.mobile.framework.exception.IllegalUserAgentException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public interface IMobileContextBuilder {

    void buildContext(MobileContext mobileContext, ServletContext servletContext,
                      HttpServletRequest request, boolean isPost, boolean postCompressed, boolean isCheckToken)
            throws IllegalUserAgentException, IllegalNewTokenException, ApplicationDecryptException;
}
