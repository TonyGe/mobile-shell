/**
 *
 */
package com.dianping.mobile.framework.exception;


/**
 * @author kewen.yao
 */
public class BuildContextException extends ApplicationException {


    private static final long serialVersionUID = -2581701564473982093L;

    public BuildContextException(Exception e) {
        super(e);
    }

    public BuildContextException(String message, Exception e) {
        super(message, e);
    }

    public BuildContextException(String message) {
        super(message);
    }
}
