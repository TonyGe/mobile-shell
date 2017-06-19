package com.dianping.mobile.framework.action;

import com.dianping.mobile.framework.datatypes.IMobileContext;
import com.dianping.mobile.framework.datatypes.IMobileResponse;
import com.dianping.mobile.framework.io.ResponseType;

public interface IAction {
	
	String getActionKey();
	
	String getHttpType();
	
	boolean postCompressed();

	boolean isCheckToken();

	ResponseType getEncryption();
	
	IMobileResponse execute(IMobileContext context);
}