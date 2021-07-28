package com.owl.integration.elasticsearch.calcite.analyzer;

public class PredicateAnalyzerException extends RuntimeException {

    public PredicateAnalyzerException(String message) {
        super(message);
    }

    public PredicateAnalyzerException(Throwable cause) {
        super(cause);
    }
}
