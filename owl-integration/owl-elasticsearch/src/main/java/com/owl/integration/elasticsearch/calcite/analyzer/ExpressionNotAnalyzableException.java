package com.owl.integration.elasticsearch.calcite.analyzer;

public class ExpressionNotAnalyzableException extends Exception {
    ExpressionNotAnalyzableException(String message, Throwable cause) {
        super(message, cause);
    }
}
