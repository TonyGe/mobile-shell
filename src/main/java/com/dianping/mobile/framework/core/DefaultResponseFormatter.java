package com.dianping.mobile.framework.core;


import com.dianping.mobile.framework.datatypes.IMobileContext;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;
import com.dianping.mobile.framework.exception.ApplicationIOException;
import com.dianping.mobile.framework.io.Formatter;
import com.dianping.mobile.framework.io.ResponseContent;

public class DefaultResponseFormatter implements IResponseFormatter {

	@Override
	public ResponseContent format(Object response, int statusCode,
			IMobileContext context) throws ApplicationIOException,
			ApplicationEncryptException {
		if (context != null) {
			return Formatter.responseFormatter(response, statusCode,
					context.getClient(), context.getVersion());
		} else {
			return Formatter
					.responseFormatter(response, statusCode, null, null);
		}
	}

	@Override
	public ResponseContent format(Object response, int statusCode)
			throws ApplicationIOException, ApplicationEncryptException {
		return Formatter.responseFormatter(response, statusCode, null, null);
	}

}
