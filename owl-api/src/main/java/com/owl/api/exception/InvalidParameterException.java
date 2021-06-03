package com.owl.api.exception;

public class InvalidParameterException extends RuntimeException {
    public final String invalidParameter;

    public String getInvalidParameter() {
        return invalidParameter;
    }

    public InvalidParameterException(String invalidParameter, String message) {
        super(message);
        this.invalidParameter = invalidParameter;
    }

    public InvalidParameterException(String invalidParameter, String message, Exception cause) {
        super(message, cause);
        this.invalidParameter = invalidParameter;
    }
}
