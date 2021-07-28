package com.owl.integration.elasticsearch.calcite.analyzer;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.NlsString;
import org.apache.calcite.util.Sarg;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        if (isSArg()) {
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

    public Object sargValues() {
        Sarg sarg = Objects.requireNonNull(literal.getValueAs(Sarg.class), "Sarg");
        Set<Range> ranges = sarg.rangeSet.asRanges();
        if (sarg.isPoints()) {
            InCollections collections = new InCollections();
            ranges.forEach(i -> collections.addItem(parseEndpointValue(i.lowerEndpoint())));
            return collections;
        } else if (sarg.isComplementedPoints()) {
            ranges = sarg.negate().rangeSet.asRanges();
            InCollections collections = new InCollections();
            collections.setNot(true);
            ranges.forEach(i -> collections.addItem(parseEndpointValue(i.lowerEndpoint())));
            return collections;
        } else {
            RangeBound rangeBound = new RangeBound();
            ranges.forEach(i -> {
                if (i.hasLowerBound()) {
                    rangeBound.setLower(parseEndpointValue(i.lowerEndpoint()));
                    rangeBound.setIncludeLower(BoundType.CLOSED == i.lowerBoundType());
                }
                if (i.hasUpperBound()) {
                    rangeBound.setUpper(parseEndpointValue(i.upperEndpoint()));
                    rangeBound.setIncludeUpper(BoundType.CLOSED == i.upperBoundType());
                }
            });
            return rangeBound;
        }
    }

    public Object rawValue() {
        return literal.getValue();
    }

    private static Object parseEndpointValue(Object endpoint) {
        if (endpoint instanceof NlsString) {
            return ((NlsString) endpoint).getValue();
        } else {
            return endpoint;
        }
    }

    public static class InCollections {
        List<Object> collections = new ArrayList<>();
        boolean not = false;

        public InCollections addItem(Object obj) {
            this.collections.add(obj);
            return this;
        }

        public List<Object> getCollections() {
            return collections;
        }

        public InCollections setCollections(List<Object> collections) {
            this.collections = collections;
            return this;
        }

        public boolean isNot() {
            return not;
        }

        public InCollections setNot(boolean not) {
            this.not = not;
            return this;
        }
    }

    public static class RangeBound {
        Object lower = null;
        Object upper = null;
        boolean includeLower;
        boolean includeUpper;

        public Object getLower() {
            return lower;
        }

        public RangeBound setLower(Object lower) {
            this.lower = lower;
            return this;
        }

        public Object getUpper() {
            return upper;
        }

        public RangeBound setUpper(Object upper) {
            this.upper = upper;
            return this;
        }

        public boolean isIncludeLower() {
            return includeLower;
        }

        public RangeBound setIncludeLower(boolean includeLower) {
            this.includeLower = includeLower;
            return this;
        }

        public boolean isIncludeUpper() {
            return includeUpper;
        }

        public RangeBound setIncludeUpper(boolean includeUpper) {
            this.includeUpper = includeUpper;
            return this;
        }
    }
}
