package com.owl.integration.elasticsearch.client.request.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-query-string-query.html
 */
public class QueryStringQuery extends Query {
    protected String query;
    protected String defaultField;
    protected String defaultOperator;
    protected String analyzer;
    protected String quoteAnalyzer;
    protected String quoteFieldSuffix;
    protected Boolean allowLeadingWildcard;
    protected Boolean enablePositionIncrements;
    protected Boolean analyzeWildcard;
    protected Boolean autoGeneratePhraseQueries;
    protected String fuzziness;
    protected Integer fuzzyPrefixLength;
    protected Integer fuzzyMaxExpansions;
    protected Integer phraseSlop;
    protected List<String> fields = new ArrayList<>();
    protected Map<String, Double> fieldBoosts = new LinkedHashMap<>();
    protected Double tieBreaker;
    protected String minimumShouldMatch;
    protected Boolean lenient;
    protected String timeZone;
    protected Integer maxDeterminizedStates;
    protected Double boost;
    protected String queryName;

    public QueryStringQuery setQuery(String query) {
        this.query = query;
        return this;
    }

    public QueryStringQuery setDefaultField(String defaultField) {
        this.defaultField = defaultField;
        return this;
    }

    public QueryStringQuery setDefaultOperator(String defaultOperator) {
        this.defaultOperator = defaultOperator;
        return this;
    }

    public QueryStringQuery setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    public QueryStringQuery setQuoteAnalyzer(String quoteAnalyzer) {
        this.quoteAnalyzer = quoteAnalyzer;
        return this;
    }

    public QueryStringQuery setQuoteFieldSuffix(String quoteFieldSuffix) {
        this.quoteFieldSuffix = quoteFieldSuffix;
        return this;
    }

    public QueryStringQuery setAllowLeadingWildcard(Boolean allowLeadingWildcard) {
        this.allowLeadingWildcard = allowLeadingWildcard;
        return this;
    }

    public QueryStringQuery setEnablePositionIncrements(Boolean enablePositionIncrements) {
        this.enablePositionIncrements = enablePositionIncrements;
        return this;
    }

    public QueryStringQuery setAnalyzeWildcard(Boolean analyzeWildcard) {
        this.analyzeWildcard = analyzeWildcard;
        return this;
    }

    public QueryStringQuery setAutoGeneratePhraseQueries(Boolean autoGeneratePhraseQueries) {
        this.autoGeneratePhraseQueries = autoGeneratePhraseQueries;
        return this;
    }

    public QueryStringQuery setFuzziness(String fuzziness) {
        this.fuzziness = fuzziness;
        return this;
    }

    public QueryStringQuery setFuzzyPrefixLength(Integer fuzzyPrefixLength) {
        this.fuzzyPrefixLength = fuzzyPrefixLength;
        return this;
    }

    public QueryStringQuery setFuzzyMaxExpansions(Integer fuzzyMaxExpansions) {
        this.fuzzyMaxExpansions = fuzzyMaxExpansions;
        return this;
    }

    public QueryStringQuery setPhraseSlop(Integer phraseSlop) {
        this.phraseSlop = phraseSlop;
        return this;
    }

    public QueryStringQuery addField(String field) {
        this.fields.add(field);
        return this;
    }

    public QueryStringQuery addFieldBoost(String field, Double boost) {
        this.fields.add(field);
        this.fieldBoosts.put(field, boost);
        return this;
    }

    public QueryStringQuery setTieBreaker(Double tieBreaker) {
        this.tieBreaker = tieBreaker;
        return this;
    }

    public QueryStringQuery setMinimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    public QueryStringQuery setMinimumShouldMatch(int minimumShouldMatch) {
        return setMinimumShouldMatch(String.valueOf(minimumShouldMatch));
    }

    public QueryStringQuery setLenient(Boolean lenient) {
        this.lenient = lenient;
        return this;
    }

    public QueryStringQuery setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public QueryStringQuery setMaxDeterminizedStates(Integer maxDeterminizedStates) {
        this.maxDeterminizedStates = maxDeterminizedStates;
        return this;
    }

    public QueryStringQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public QueryStringQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public String getDefaultField() {
        return defaultField;
    }

    public String getDefaultOperator() {
        return defaultOperator;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public String getQuoteAnalyzer() {
        return quoteAnalyzer;
    }

    public String getQuoteFieldSuffix() {
        return quoteFieldSuffix;
    }

    public Boolean getAllowLeadingWildcard() {
        return allowLeadingWildcard;
    }

    public Boolean getEnablePositionIncrements() {
        return enablePositionIncrements;
    }

    public Boolean getAnalyzeWildcard() {
        return analyzeWildcard;
    }

    public Boolean getAutoGeneratePhraseQueries() {
        return autoGeneratePhraseQueries;
    }

    public String getFuzziness() {
        return fuzziness;
    }

    public Integer getFuzzyPrefixLength() {
        return fuzzyPrefixLength;
    }

    public Integer getFuzzyMaxExpansions() {
        return fuzzyMaxExpansions;
    }

    public Integer getPhraseSlop() {
        return phraseSlop;
    }

    public List<String> getFields() {
        return fields;
    }

    public Map<String, Double> getFieldBoosts() {
        return fieldBoosts;
    }

    public Double getTieBreaker() {
        return tieBreaker;
    }

    public String getMinimumShouldMatch() {
        return minimumShouldMatch;
    }

    public Boolean getLenient() {
        return lenient;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public Integer getMaxDeterminizedStates() {
        return maxDeterminizedStates;
    }

    public Double getBoost() {
        return boost;
    }

    public String getQueryName() {
        return queryName;
    }

    /*{
            "query_string" : {
                "default_field" : "content",
                "query" : "this AND that OR thus"
            }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("query", this.query);
        if(this.defaultField != null)  params.put("default_field", this.defaultField);
        if(this.defaultOperator != null)  params.put("default_operator", this.defaultOperator);
        if(this.analyzer != null)  params.put("analyzer", this.analyzer);
        if(this.quoteAnalyzer != null)  params.put("quote_analyzer", this.quoteAnalyzer);
        if(this.quoteFieldSuffix != null)  params.put("quote_field_suffix", this.quoteFieldSuffix);
        if(this.allowLeadingWildcard != null)  params.put("allow_leading_wildcard", this.allowLeadingWildcard);
        if(this.enablePositionIncrements != null)  params.put("enable_position_increments", this.enablePositionIncrements);
        if(this.analyzeWildcard != null)  params.put("analyze_wildcard", this.analyzeWildcard);
        if(this.fuzziness != null)  params.put("fuzziness", this.fuzziness);
        if(this.fuzzyPrefixLength != null)  params.put("fuzzy_prefix_length", this.fuzzyPrefixLength);
        if(this.fuzzyMaxExpansions != null)  params.put("fuzzy_max_expansions", this.fuzzyMaxExpansions);
        if(this.phraseSlop != null)  params.put("phrase_slop", this.phraseSlop);
        if(this.tieBreaker != null)  params.put("tie_breaker", this.tieBreaker);
        if(this.minimumShouldMatch != null)  params.put("minimum_should_match", this.minimumShouldMatch);
        if(this.lenient != null)  params.put("lenient", this.lenient);
        if(this.timeZone != null)  params.put("time_zone", this.timeZone);
        if(this.maxDeterminizedStates != null)  params.put("max_determinized_states", this.maxDeterminizedStates);
        if(this.boost != null) params.put("boost", this.boost);
        if(this.queryName != null) params.put("_name", this.queryName);
        if(this.fields.size() > 0) {
            List<String> array = new ArrayList<>();
            for(String field : fields) {
                if(this.fieldBoosts.containsKey(field)) {
                    array.add(String.format("%s^%s", field, String.valueOf(this.fieldBoosts.get(field))));
                } else {
                    array.add(field);
                }
            }
            params.put("fields", array);
        }
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("query_string", params);
        return query;
    }
}
