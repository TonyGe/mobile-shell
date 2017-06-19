package com.dianping.mobile.framework.core;

import com.dianping.mobile.framework.datatypes.IMobileContext;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;
import com.dianping.mobile.framework.exception.ApplicationIOException;
import com.dianping.mobile.framework.io.Formatter;
import com.dianping.mobile.framework.io.ResponseContent;

/**
 * @author yangzhongwei
 * @date 2014-11-18 19:11
 */
public class StringFormatter implements IResponseFormatter {
    @Override
    public ResponseContent format(Object response, int statusCode, IMobileContext context) throws ApplicationIOException, ApplicationEncryptException {
        return Formatter.stringFormatter(response, statusCode);
    }

    @Override
    public ResponseContent format(Object response, int statusCode) throws ApplicationIOException, ApplicationEncryptException {
        return Formatter.stringFormatter(response, statusCode);
    }
}
