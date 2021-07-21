package com.owl.api.exception;

public class InvalidTypeException extends RuntimeException {
    public InvalidTypeException() {
    }

    public InvalidTypeException(String message) {
        super(message);
    }

    public InvalidTypeException(String message, Throwable ex) {
        super(message, ex);
    }
}
