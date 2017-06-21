package com.dianping.mobile.framework.exception;

public class ApplicationEncryptException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -334838807824902794L;

    public ApplicationEncryptException() {
    }

    public ApplicationEncryptException(String message) {
        super(message);
    }

    public ApplicationEncryptException(Throwable cause) {
        super(cause);
    }

    public ApplicationEncryptException(String message, Throwable cause) {
        super(message, cause);
    }

}
