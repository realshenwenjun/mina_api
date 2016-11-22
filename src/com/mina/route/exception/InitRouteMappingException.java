package com.mina.route.exception;

/**
 * Created by Administrator on 2016/11/20.
 */
public class InitRouteMappingException extends Exception {

    public InitRouteMappingException() {
    }

    public InitRouteMappingException(String message) {
        super(message);
    }

    public InitRouteMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitRouteMappingException(Throwable cause) {
        super(cause);
    }

    public InitRouteMappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
