package com.owl.integration.elasticsearch.calcite.analyzer;

import com.owl.integration.elasticsearch.client.request.query.QueryBuilders;
import com.owl.integration.elasticsearch.client.request.query.BoolQuery;
import com.owl.integration.elasticsearch.client.request.query.Query;

/**
 * Builds conjunctions / disjunctions based on existing expressions.
 */
public class CompoundQueryExpression extends QueryExpression {
    public final boolean partial;
    public final BoolQuery query;

    public CompoundQueryExpression(boolean partial) {
        this(partial, QueryBuilders.boolQuery());
    }

    public CompoundQueryExpression(boolean partial, BoolQuery query) {
        this.partial = partial;
        this.query = query;
    }

    public static CompoundQueryExpression or(QueryExpression... expressions) {
        CompoundQueryExpression bqe = new CompoundQueryExpression(false);
        for (QueryExpression expression : expressions) {
            bqe.query.addShould(expression.query());
        }
        return bqe;
    }

    /**
     * If partial expression, we will need to complete it with a full filter.
     *
     * @param partial whether we partially converted a and for push down purposes
     * @param expressions list of expressions to join with {@code and} boolean
     * @return new instance of expression
     */
    public static CompoundQueryExpression and(boolean partial, QueryExpression... expressions) {
        CompoundQueryExpression bqe = new CompoundQueryExpression(partial);
        for (QueryExpression expression : expressions) {
            if (expression != null) { // partial expressions have nulls for missing nodes
                bqe.query.addMust(expression.query());
            }
        }
        return bqe;
    }

    @Override
    public Query query() {
        return query;
    }

    @Override
    public QueryExpression not() {
        return new CompoundQueryExpression(partial, QueryBuilders.boolQuery().addMustNot(query()));
    }

    @Override
    public QueryExpression contains(LiteralExpression literal) {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['contains'] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression exists() {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['exists'] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression notExists() {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['notExists'] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression like(LiteralExpression literal) {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['like'] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression notLike(LiteralExpression literal) {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['notLike'] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression equals(LiteralExpression literal) {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['='] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression notEquals(LiteralExpression literal) {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['not'] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression gt(LiteralExpression literal) {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['>'] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression gte(LiteralExpression literal) {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['>='] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression lt(LiteralExpression literal) {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['<'] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression lte(LiteralExpression literal) {
        throw new PredicateAnalyzerException("SqlOperatorImpl ['<='] "
                + "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression queryString(String query) {
        throw new PredicateAnalyzerException("QueryString " +
                "cannot be applied to a compound expression");
    }

    @Override
    public QueryExpression isTrue() {
        throw new PredicateAnalyzerException("isTrue " +
                "cannot be applied to a compound expression");
    }
}
