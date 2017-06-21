package com.dianping.mobile.framework.exception;

public class ApplicationDecryptException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -334838807824902794L;

    public ApplicationDecryptException() {
    }

    public ApplicationDecryptException(String message) {
        super(message);
    }

    public ApplicationDecryptException(Throwable cause) {
        super(cause);
    }

    public ApplicationDecryptException(String message, Throwable cause) {
        super(message, cause);
    }

}
