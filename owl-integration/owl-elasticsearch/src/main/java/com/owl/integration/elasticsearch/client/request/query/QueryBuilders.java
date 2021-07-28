package com.owl.integration.elasticsearch.client.request.query;

import java.util.List;

public class QueryBuilders {

    /**
     * A Query that matches documents matching boolean combinations of other queries.
     */
    public static BoolQuery boolQuery() {
        return new BoolQuery();
    }

    /**
     * A query that wraps another query and simply returns a constant score equal to the
     * query boost for every document in the query.
     *
     * @param query The query to wrap in a constant score query
     */
    public static ConstantScoreQuery constantScoreQuery(Query query) {
        return new ConstantScoreQuery().setQuery(query);
    }

    /**
     * A filter to filter only documents where a field exists in them.
     *
     * @param name The name of the field
     */
    public static ExistsQuery existsQuery(String name) {
        return new ExistsQuery().setField(name);
    }

    /**
     * A Query that matches documents containing a term.
     *
     * @param name  The name of the field
     * @param value The value of the term
     */
    public static MatchQuery matchQuery(String name, Object value) {
        return new MatchQuery().setField(name).setQuery(value);
    }

    /**
     * A Query that has prefix match with a specified prefix.
     *
     * @param name   The name of the field
     * @param prefix The prefix
     */
    public static PrefixQuery prefixQuery(String name, String prefix) {
        return new PrefixQuery().setField(name).setPrefix(prefix);
    }

    /**
     *  A Query that has wildcard match with a specified wildcard.
     * @param name     The name of the field
     * @param wildcard The wildcard
     */
    public static WildcardQuery wildcardQuery(String name, String wildcard) {
        return new WildcardQuery().setField(name).setWildcard(wildcard);
    }

    /**
     * A Query that matches documents containing terms with a specified regular expression.
     *
     * @param name   The name of the field
     * @param regexp The regular expression
     */
    public static RegexpQuery regexpQuery(String name, String regexp) {
        return new RegexpQuery().setField(name).setRegexp(regexp);
    }

    /**
     * A Query that matches documents within an range of terms.
     *
     * @param name The field name
     */
    public static RangeQuery rangeQuery(String name) {
        return new RangeQuery().setField(name);
    }

    /**
     * A Query that matches documents containing a term.
     *
     * @param name  The name of the field
     * @param value The value of the term
     */
    public static TermQuery termQuery(String name, Object value) {
        return new TermQuery().setField(name).setTerm(value);
    }

    /**
     * A Query that matches documents containing a terms.
     *
     * @param name  The name of the field
     * @param value The value of the terms
     */
    public static TermsQuery termsQuery(String name, List<Object> value) {
        return new TermsQuery().setField(name).setTerms(value);
    }

    /**
     * A Query that matches documents by queryString.
     *
     * @param value The query string
     */
    public static QueryStringQuery queryString(String value) {
        return new QueryStringQuery().setQuery(value);
    }
}
