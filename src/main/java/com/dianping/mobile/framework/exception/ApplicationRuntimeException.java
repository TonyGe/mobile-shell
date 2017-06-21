/**
 *
 */
package com.dianping.mobile.framework.exception;

/**
 * @author kewen.yao
 */
public class ApplicationRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 378693506676728715L;

    public ApplicationRuntimeException(Exception e) {
        super(e);
    }

    public ApplicationRuntimeException(String message, Exception e) {
        super(message, e);
    }

    public ApplicationRuntimeException(String message) {
        super(message);
    }

}
