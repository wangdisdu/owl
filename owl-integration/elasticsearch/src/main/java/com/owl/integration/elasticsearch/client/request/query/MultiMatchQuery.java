package com.owl.integration.elasticsearch.client.request.query;

import com.owl.common.EasyUtil;

import java.util.*;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-multi-match-query.html
 */
public class MultiMatchQuery extends Query {
    public final static String BestFields = "best_fields";
    public final static String MostFields = "most_fields";
    public final static String CrossFields = "cross_fields";
    public final static String Phrase = "phrase";


    protected Object query;
    protected HashSet<String> fields = new LinkedHashSet<>();
    protected String type;
    protected String operator;
    protected String analyzer;
    protected String minimumShouldMatch;
    protected Boolean lenient;
    protected String fuzziness;
    protected String rewrite;
    protected String fuzzyRewrite;
    protected Double tieBreaker;
    protected Integer prefixLength;
    protected Integer maxExpansions;
    protected String zeroTermsQuery;
    protected Double cutoffFrequency;
    protected Integer slop;
    protected Double boost;
    protected String queryName;

    public MultiMatchQuery setQuery(Object query) {
        this.query = query;
        return this;
    }

    public MultiMatchQuery addField(String field) {
        this.fields.add(field);
        return this;
    }

    public MultiMatchQuery addFieldBoost(String field, Double boost) {
        this.fields.add(field + "^" + EasyUtil.formatDouble(boost));
        return this;
    }

    // getType can be "best_fields", "most_fields", "cross_fields", "phrase", or "phrase_prefix".
    public MultiMatchQuery setType(String type) {
        this.type = type;
        return this;
    }

    // Operator can be either AND or OR (default).
    public MultiMatchQuery setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public MultiMatchQuery setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    public MultiMatchQuery setMinimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    public MultiMatchQuery setMinimumShouldMatch(int minimumShouldMatch) {
        return this.setMinimumShouldMatch(String.valueOf(minimumShouldMatch));
    }

    public MultiMatchQuery setLenient(Boolean lenient) {
        this.lenient = lenient;
        return this;
    }

    public MultiMatchQuery setFuzziness(String fuzziness) {
        this.fuzziness = fuzziness;
        return this;
    }

    public MultiMatchQuery setRewrite(String rewrite) {
        this.rewrite = rewrite;
        return this;
    }

    public MultiMatchQuery setFuzzyRewrite(String fuzzyRewrite) {
        this.fuzzyRewrite = fuzzyRewrite;
        return this;
    }

    public MultiMatchQuery setTieBreaker(Double tieBreaker) {
        this.tieBreaker = tieBreaker;
        return this;
    }

    public MultiMatchQuery setPrefixLength(Integer prefixLength) {
        this.prefixLength = prefixLength;
        return this;
    }

    public MultiMatchQuery setMaxExpansions(Integer maxExpansions) {
        this.maxExpansions = maxExpansions;
        return this;
    }

    public MultiMatchQuery setZeroTermsQuery(String zeroTermsQuery) {
        this.zeroTermsQuery = zeroTermsQuery;
        return this;
    }

    public MultiMatchQuery setCutoffFrequency(Double cutoffFrequency) {
        this.cutoffFrequency = cutoffFrequency;
        return this;
    }

    public MultiMatchQuery setSlop(Integer slop) {
        this.slop = slop;
        return this;
    }

    public MultiMatchQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public MultiMatchQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public Object getQuery() {
        return query;
    }

    public List<String> getFields() {
        return new ArrayList<>(fields);
    }

    public String getType() {
        return type;
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

    public String getRewrite() {
        return rewrite;
    }

    public String getFuzzyRewrite() {
        return fuzzyRewrite;
    }

    public Double getTieBreaker() {
        return tieBreaker;
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

    public Integer getSlop() {
        return slop;
    }

    public Double getBoost() {
        return boost;
    }

    public String getQueryName() {
        return queryName;
    }

    // {
    //   "multi_match" : {
    //     "query" : "this is a test",
    //     "fields" : [ "subject", "message" ]
    //   }
    // }
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("query", query);
        if(fields.size() > 0) params.put("fields", fields);
        if(type != null) params.put("type", type);
        if(operator != null) params.put("operator", operator);
        if(analyzer != null) params.put("analyzer", analyzer);
        if(boost != null) params.put("boost", boost);
        if(slop != null) params.put("slop", slop);
        if(fuzziness != null) params.put("fuzziness", fuzziness);
        if(prefixLength != null) params.put("prefix_length", prefixLength);
        if(maxExpansions != null) params.put("max_expansions", maxExpansions);
        if(minimumShouldMatch != null) params.put("minimum_should_match", minimumShouldMatch);
        if(rewrite != null) params.put("rewrite", rewrite);
        if(fuzzyRewrite != null) params.put("fuzzy_rewrite", fuzzyRewrite);
        if(tieBreaker != null) params.put("tie_breaker", tieBreaker);
        if(lenient != null) params.put("lenient", lenient);
        if(cutoffFrequency != null) params.put("cutoff_frequency", cutoffFrequency);
        if(zeroTermsQuery != null) params.put("zero_terms_query", zeroTermsQuery);
        if(this.queryName != null) params.put("_name", this.queryName);

        Map<String, Object> mq = new LinkedHashMap<>();
        mq.put("multi_match", params);
        return mq;
    }
}
