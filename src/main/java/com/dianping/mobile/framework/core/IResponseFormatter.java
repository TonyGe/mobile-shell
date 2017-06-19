package com.dianping.mobile.framework.core;

import com.dianping.mobile.framework.datatypes.IMobileContext;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;
import com.dianping.mobile.framework.exception.ApplicationIOException;
import com.dianping.mobile.framework.io.ResponseContent;

public interface IResponseFormatter {

	ResponseContent format(Object response, int statusCode,
			IMobileContext context) throws ApplicationIOException,
			ApplicationEncryptException;

	ResponseContent format(Object response, int statusCode)
			throws ApplicationIOException, ApplicationEncryptException;

}
