package com.owl.integration.elasticsearch.client.request.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-match-query.html
 */
public class MatchQuery extends Query {
    protected String field;
    protected Object query;
    protected String operator;
    protected String analyzer;
    protected String minimumShouldMatch;
    protected Boolean lenient;
    protected String fuzziness;
    protected Integer prefixLength;
    protected Integer maxExpansions;
    protected String zeroTermsQuery;
    protected Double cutoffFrequency;
    protected Double boost;
    protected String queryName;

    public MatchQuery setField(String field) {
        this.field = field;
        return this;
    }

    public MatchQuery setQuery(Object query) {
        this.query = query;
        return this;
    }

    public MatchQuery setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public MatchQuery setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    public MatchQuery setMinimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    public MatchQuery setMinimumShouldMatch(int minimumShouldMatch) {
        return this.setMinimumShouldMatch(String.valueOf(minimumShouldMatch));
    }

    public MatchQuery setLenient(Boolean lenient) {
        this.lenient = lenient;
        return this;
    }

    public MatchQuery setFuzziness(String fuzziness) {
        this.fuzziness = fuzziness;
        return this;
    }

    public MatchQuery setPrefixLength(Integer prefixLength) {
        this.prefixLength = prefixLength;
        return this;
    }

    public MatchQuery setMaxExpansions(Integer maxExpansions) {
        this.maxExpansions = maxExpansions;
        return this;
    }

    public MatchQuery setZeroTermsQuery(String zeroTermsQuery) {
        this.zeroTermsQuery = zeroTermsQuery;
        return this;
    }

    public MatchQuery setCutoffFrequency(Double cutoffFrequency) {
        this.cutoffFrequency = cutoffFrequency;
        return this;
    }

    public MatchQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public MatchQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public String getField() {
        return field;
    }

    public Object getQuery() {
        return query;
    }

    public String getOperator() {
        return operator;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public String getMinimumShouldMatch() {
        return minimumShouldMatch;
    }

    public Boolean getLenient() {
        return lenient;
    }

    public String getFuzziness() {
        return fuzziness;
    }

    public Integer getPrefixLength() {
        return prefixLength;
    }

    public Integer getMaxExpansions() {
        return maxExpansions;
    }

    public String getZeroTermsQuery() {
        return zeroTermsQuery;
    }

    public Double getCutoffFrequency() {
        return cutoffFrequency;
    }

    public Double getBoost() {
        return boost;
    }

    public String getQueryName() {
        return queryName;
    }

    /*{
            "match" : {
                "message" : {
                    "query" : "this is a test",
                    "operator" : "and"
                }
            }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("query", this.query);
        if(this.operator != null)  params.put("operator", this.operator);
        if(this.analyzer != null)  params.put("analyzer", this.analyzer);
        if(this.minimumShouldMatch != null)  params.put("minimum_should_match", this.minimumShouldMatch);
        if(this.lenient != null)  params.put("lenient", this.lenient);
        if(this.fuzziness != null)  params.put("fuzziness", this.fuzziness);
        if(this.prefixLength != null)  params.put("prefix_length", this.prefixLength);
        if(this.maxExpansions != null)  params.put("max_expansions", this.maxExpansions);
        if(this.zeroTermsQuery != null)  params.put("zero_terms_query", this.zeroTermsQuery);
        if(this.cutoffFrequency != null)  params.put("cutoff_frequency", this.cutoffFrequency);
        if(this.boost != null) params.put("boost", this.boost);
        if(this.queryName != null) params.put("_name", this.queryName);

        Map<String, Object> match = new LinkedHashMap<>();
        match.put(this.field, params);
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("match", match);
        return query;
    }
}
