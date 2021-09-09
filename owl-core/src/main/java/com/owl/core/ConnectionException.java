package com.owl.core;

public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable ex) {
        super(message, ex);
    }
}
