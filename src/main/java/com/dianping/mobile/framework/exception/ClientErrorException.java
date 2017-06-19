/**
 * 
 */
package com.dianping.mobile.framework.exception;


/**
 * @author kewen.yao
 *
 */
public class ClientErrorException extends ApplicationRuntimeException {

	private static final long serialVersionUID = -1477085718429106584L;
	
	public ClientErrorException(Exception e) {
		super(e);
	}
	public ClientErrorException(String message, Exception e) {
		super(message, e);
	}
	
	public ClientErrorException(String message) {
		super(message);
	}

}
