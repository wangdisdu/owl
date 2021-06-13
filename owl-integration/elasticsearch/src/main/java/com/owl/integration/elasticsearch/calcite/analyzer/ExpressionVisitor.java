package com.owl.integration.elasticsearch.calcite.analyzer;

import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexVisitorImpl;

import java.util.List;

public class ExpressionVisitor extends RexVisitorImpl<String> {
    private final List<String> inFields;

    public ExpressionVisitor(List<String> inFields) {
        super(true);
        this.inFields = inFields;
    }

    @Override
    public String visitInputRef(RexInputRef inputRef) {
        return inFields.get(inputRef.getIndex());
    }

    @Override
    public String visitLiteral(RexLiteral literal) {
        throw new IllegalArgumentException("Expression of " + literal
                + " is not supported by ElasticsearchProject");
    }

    @Override
    public String visitCall(RexCall call) {
        throw new IllegalArgumentException("Expression of " + call
                + " is not supported by ElasticsearchProject");
    }
}
