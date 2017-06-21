/**
 *
 */
package com.dianping.mobile.framework.exception;


/**
 * @author kewen.yao
 */
public class HttpTypeIllegalException extends ApplicationException {

    private static final long serialVersionUID = 1745852997930113453L;

    public HttpTypeIllegalException(Exception e) {
        super(e);
    }

    public HttpTypeIllegalException(String message, Exception e) {
        super(message, e);
    }

    public HttpTypeIllegalException(String message) {
        super(message);
    }

}
