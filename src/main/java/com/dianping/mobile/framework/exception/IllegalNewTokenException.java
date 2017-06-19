/**
 * 
 */
package com.dianping.mobile.framework.exception;

/**
 * @author kewen.yao
 *
 */
public class IllegalNewTokenException extends BuildContextException {
	
	private static final long serialVersionUID = 2634076422389403912L;

	public IllegalNewTokenException(Exception e) {
		super(e);
	}
	public IllegalNewTokenException(String message, Exception e) {
		super(message, e);
	}

	public IllegalNewTokenException(String message) {
		super(message);
	}
}
