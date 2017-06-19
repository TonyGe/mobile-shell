package com.dianping.mobile.framework.exception;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = -2014018672153093716L;

	public ApplicationException(Exception e) {
		super(e);
	}

	public ApplicationException(String message, Exception e) {
		super(message, e);
	}

	public ApplicationException(String message) {
		super(message);
	}
}