/**
 *
 */
package com.dianping.mobile.framework.exception;

/**
 * @author kewen.yao
 */
public class IllegalUserAgentException extends BuildContextException {

    private static final long serialVersionUID = 5021831837852248051L;

    public IllegalUserAgentException(Exception e) {
        super(e);
    }

    public IllegalUserAgentException(String message, Exception e) {
        super(message, e);
    }

    public IllegalUserAgentException(String message) {
        super(message);
    }

}
