package com.owl.integration.elasticsearch.client;

public class EsCallException extends RuntimeException {
    public EsCallException(String message) {
        super(message);
    }

    public EsCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public EsCallException(Throwable cause) {
        super(cause);
    }
}
