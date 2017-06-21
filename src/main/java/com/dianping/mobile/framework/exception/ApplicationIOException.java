/**
 *
 */
package com.dianping.mobile.framework.exception;


/**
 * @author kewen.yao
 */
public class ApplicationIOException extends ApplicationException {

    private static final long serialVersionUID = 3742696268864548779L;

    public ApplicationIOException(Exception e) {
        super(e);
    }

    public ApplicationIOException(String message, Exception e) {
        super(message, e);
    }

    public ApplicationIOException(String message) {
        super(message);
    }

}
