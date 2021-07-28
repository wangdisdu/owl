package com.owl.integration.elasticsearch.calcite.analyzer;

import com.owl.integration.elasticsearch.Constants;
import org.apache.calcite.rex.RexLiteral;

/**
 * Used for bind variables.
 */
public class NamedFieldExpression implements TerminalExpression {

    public final String name;

    public NamedFieldExpression(String name) {
        this.name = name;
    }

    public NamedFieldExpression(RexLiteral literal) {
        this.name = literal == null ? null : RexLiteral.stringValue(literal);
    }

    public String getRootName() {
        return name;
    }

    public boolean isMetaField() {
        return Constants.META_COLUMNS.contains(getRootName());
    }

    public String getReference() {
        return getRootName();
    }
}
