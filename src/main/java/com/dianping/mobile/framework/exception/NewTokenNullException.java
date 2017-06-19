/**
 * 
 */
package com.dianping.mobile.framework.exception;

/**
 * @author kewen.yao
 *
 */
public class NewTokenNullException extends BuildContextException {
	
	private static final long serialVersionUID = 2986612842870006852L;

	public NewTokenNullException(Exception e) {
		super(e);
	}
	public NewTokenNullException(String message, Exception e) {
		super(message, e);
	}

	public NewTokenNullException(String message) {
		super(message);
	}
	
}
