package com.test.carparking.exception;

/**
 * @author NIsaev on 20.08.2020
 */
public class NotFreeException extends Exception {

    public NotFreeException(String msg) {
        super(msg);
    }

    public NotFreeException(String msg, Exception e) {
        super(msg + " because of " + e.toString());
    }
}
