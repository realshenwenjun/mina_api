package com.mina.core.exception;

/**
 * Created by ASUS on 2016/11/18.
 */
public class InfException extends Exception {

    public InfException() {
    }

    public InfException(String message) {
        super(message);
    }

    public InfException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfException(Throwable cause) {
        super(cause);
    }

    public InfException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
