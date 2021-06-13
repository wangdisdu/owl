package com.owl.integration.elasticsearch.calcite.analyzer;

import org.apache.calcite.rel.type.RelDataType;

/**
 * SQL cast. For example, {@code cast(col as INTEGER)}.
 */
public class CastExpression implements TerminalExpression {
    public final RelDataType type;
    public final TerminalExpression argument;

    public CastExpression(RelDataType type, TerminalExpression argument) {
        this.type = type;
        this.argument = argument;
    }

    public boolean isCastFromLiteral() {
        return argument instanceof LiteralExpression;
    }

    public static TerminalExpression unpack(TerminalExpression exp) {
        if (!(exp instanceof CastExpression)) {
            return exp;
        }
        return ((CastExpression) exp).argument;
    }

    public static boolean isCastExpression(Expression exp) {
        return exp instanceof CastExpression;
    }
}
