package com.owl.integration.elasticsearch.calcite.analyzer;

import com.google.common.collect.Range;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.NlsString;
import org.apache.calcite.util.Sarg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Literal like {@code 'foo' or 42 or true} etc.
 */
public class LiteralExpression implements TerminalExpression {

    public final RexLiteral literal;

    public LiteralExpression(RexLiteral literal) {
        this.literal = literal;
    }

    public Object value() {
        if(isSArg()) {
            return sargValues();
        } else if (isIntegral()) {
            return longValue();
        } else if (isFloatingPoint()) {
            return doubleValue();
        } else if (isBoolean()) {
            return booleanValue();
        } else if (isString()) {
            return RexLiteral.stringValue(literal);
        } else {
            return rawValue();
        }
    }

    public boolean isSArg() {
        return SqlTypeName.SARG == literal.getTypeName();
    }

    public boolean isIntegral() {
        return SqlTypeName.INT_TYPES.contains(literal.getType().getSqlTypeName());
    }

    public boolean isFloatingPoint() {
        return SqlTypeName.APPROX_TYPES.contains(literal.getType().getSqlTypeName());
    }

    public boolean isBoolean() {
        return SqlTypeName.BOOLEAN_TYPES.contains(literal.getType().getSqlTypeName());
    }

    public boolean isString() {
        return SqlTypeName.CHAR_TYPES.contains(literal.getType().getSqlTypeName());
    }

    public long longValue() {
        return ((Number) literal.getValue()).longValue();
    }

    public double doubleValue() {
        return ((Number) literal.getValue()).doubleValue();
    }

    public boolean booleanValue() {
        return RexLiteral.booleanValue(literal);
    }

    public String stringValue() {
        return RexLiteral.stringValue(literal);
    }

    public List<String> sargValues() {
        Set<String> values = new HashSet<>();
        Sarg sarg = (Sarg) literal.getValue();
        Set<Range> ranges = sarg.rangeSet.asRanges();
        ranges.forEach(i -> {
            if(i.hasLowerBound()) {
                Object lower = i.lowerEndpoint();
                if(lower instanceof NlsString) {
                    values.add(((NlsString) lower).getValue());
                }
            }
            if(i.hasUpperBound()) {
                Object upper = i.upperEndpoint();
                if(upper instanceof NlsString) {
                    values.add(((NlsString) upper).getValue());
                }
            }
        });
        return new ArrayList<>(values);
    }

    public Object rawValue() {
        return literal.getValue();
    }
}
