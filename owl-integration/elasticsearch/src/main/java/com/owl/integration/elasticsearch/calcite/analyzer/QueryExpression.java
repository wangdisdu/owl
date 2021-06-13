package com.owl.integration.elasticsearch.calcite.analyzer;

import com.owl.integration.elasticsearch.client.request.query.Query;

import java.util.Locale;

/**
 * Main expression operators (like {@code equals}, {@code gt}, {@code exists} etc.)
 */
public abstract class QueryExpression implements Expression {
    public abstract Query query();

    public boolean isPartial() {
        return false;
    }

    /**
     * Negate {@code this} QueryExpression (not the next one).
     */
    public abstract QueryExpression not();

    public abstract QueryExpression contains(LiteralExpression literal);

    public abstract QueryExpression exists();

    public abstract QueryExpression notExists();

    public abstract QueryExpression like(LiteralExpression literal);

    public abstract QueryExpression notLike(LiteralExpression literal);

    public abstract QueryExpression equals(LiteralExpression literal);

    public abstract QueryExpression notEquals(LiteralExpression literal);

    public abstract QueryExpression gt(LiteralExpression literal);

    public abstract QueryExpression gte(LiteralExpression literal);

    public abstract QueryExpression lt(LiteralExpression literal);

    public abstract QueryExpression lte(LiteralExpression literal);

    public abstract QueryExpression queryString(String query);

    public abstract QueryExpression isTrue();

    public static QueryExpression create(TerminalExpression expression) {

        if (expression instanceof NamedFieldExpression) {
            return new SimpleQueryExpression((NamedFieldExpression) expression);
        } else {
            String message = String.format(Locale.ROOT, "Unsupported expression: [%s]", expression);
            throw new PredicateAnalyzerException(message);
        }
    }
}
