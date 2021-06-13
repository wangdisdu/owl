package com.owl.integration.elasticsearch.calcite.analyzer;

import com.owl.integration.elasticsearch.client.request.query.QueryBuilders;
import com.owl.integration.elasticsearch.client.request.query.Query;
import com.owl.integration.elasticsearch.client.request.query.RangeQuery;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Usually basic expression of type {@code a = 'val'} or {@code b > 42}.
 */
public class SimpleQueryExpression extends QueryExpression {
    public final NamedFieldExpression rel;
    public Query query;

    public SimpleQueryExpression(NamedFieldExpression rel) {
        this.rel = rel;
    }

    private String getFieldReference() {
        return rel.getReference();
    }

    @Override
    public Query query() {
        if (query == null) {
            throw new IllegalStateException("Builder was not initialized");
        }
        return query;
    }

    @Override
    public QueryExpression not() {
        query = QueryBuilders.boolQuery().addMustNot(query());
        return this;
    }

    @Override
    public QueryExpression contains(LiteralExpression literal) {
        query = QueryBuilders.matchQuery(getFieldReference(), literal.value());
        return this;
    }

    @Override
    public QueryExpression exists() {
        query = QueryBuilders.existsQuery(getFieldReference());
        return this;
    }

    @Override
    public QueryExpression notExists() {
        query = QueryBuilders.boolQuery().addMustNot(QueryBuilders.existsQuery(getFieldReference()));
        return this;
    }

    @Override
    public QueryExpression like(LiteralExpression literal) {
        query = QueryBuilders.regexpQuery(getFieldReference(), literal.stringValue());
        return this;
    }

    @Override
    public QueryExpression notLike(LiteralExpression literal) {
        query = QueryBuilders.boolQuery()
                .addMust(QueryBuilders.existsQuery(getFieldReference()))
                .addMustNot(QueryBuilders.regexpQuery(getFieldReference(), literal.stringValue()));
        return this;
    }

    @Override
    public QueryExpression equals(LiteralExpression literal) {
        Object value = literal.value();
        if (value instanceof GregorianCalendar) {
            query = QueryBuilders.boolQuery()
                    .addMust(addFormatIfNecessary(literal, QueryBuilders.rangeQuery(getFieldReference()).setGTE(value)))
                    .addMust(addFormatIfNecessary(literal, QueryBuilders.rangeQuery(getFieldReference()).setLTE(value)));
        } else if(value instanceof List) {
            query = QueryBuilders.termsQuery(getFieldReference(), (List<Object>) value);
        } else {
            query = QueryBuilders.termQuery(getFieldReference(), value);
        }
        return this;
    }

    @Override
    public QueryExpression notEquals(LiteralExpression literal) {
        Object value = literal.value();
        if (value instanceof GregorianCalendar) {
            query = QueryBuilders.boolQuery()
                    .addShould(addFormatIfNecessary(literal, QueryBuilders.rangeQuery(getFieldReference()).setGT(value)))
                    .addShould(addFormatIfNecessary(literal, QueryBuilders.rangeQuery(getFieldReference()).setLT(value)));
        } else {
            query = QueryBuilders.boolQuery()
                    .addMust(QueryBuilders.existsQuery(getFieldReference()))
                    .addMustNot(QueryBuilders.termQuery(getFieldReference(), value));
        }
        return this;
    }

    @Override
    public QueryExpression gt(LiteralExpression literal) {
        Object value = literal.value();
        query = addFormatIfNecessary(literal, QueryBuilders.rangeQuery(getFieldReference()).setGT(value));
        return this;
    }

    @Override
    public QueryExpression gte(LiteralExpression literal) {
        Object value = literal.value();
        query = addFormatIfNecessary(literal, QueryBuilders.rangeQuery(getFieldReference()).setGTE(value));
        return this;
    }

    @Override
    public QueryExpression lt(LiteralExpression literal) {
        Object value = literal.value();
        query = addFormatIfNecessary(literal, QueryBuilders.rangeQuery(getFieldReference()).setLT(value));
        return this;
    }

    @Override
    public QueryExpression lte(LiteralExpression literal) {
        Object value = literal.value();
        query = addFormatIfNecessary(literal, QueryBuilders.rangeQuery(getFieldReference()).setLTE(value));
        return this;
    }

    @Override
    public QueryExpression queryString(String string) {
        query = QueryBuilders.queryString(string);
        return this;
    }

    @Override
    public QueryExpression isTrue() {
        query = QueryBuilders.termQuery(getFieldReference(), true);
        return this;
    }

    /**
     * By default, range queries on date/time need use the format of the source to parse the literal.
     * So we need to specify that the literal has "date_time" format
     * @param literal literal value
     * @param rangeQuery query builder to optionally add {@code format} expression
     * @return existing builder with possible {@code format} attribute
     */
    private static RangeQuery addFormatIfNecessary(LiteralExpression literal,
                                                   RangeQuery rangeQuery) {
        if (literal.value() instanceof GregorianCalendar) {
            rangeQuery.setFormat("date_time");
        }
        return rangeQuery;
    }
}
