package com.test.carparking.exception;

/**
 * @author NIsaev on 20.08.2020
 */
public class NotBlockedException extends Exception {
    public NotBlockedException(String msg) {
        super(msg);
    }

    public NotBlockedException(String msg, Exception e) {
        super(msg + " because of " + e.toString());
    }
}
