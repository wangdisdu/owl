package com.owl.integration.elasticsearch.client.request.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-match-query-phrase.html
 */
public class MatchPhraseQuery extends Query {
    protected String field;
    protected Object query;
    protected String analyzer;
    protected Integer slop;
    protected Double boost;
    protected String queryName;

    public MatchPhraseQuery setField(String field) {
        this.field = field;
        return this;
    }

    public MatchPhraseQuery setQuery(Object query) {
        this.query = query;
        return this;
    }

    public MatchPhraseQuery setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    public MatchPhraseQuery setSlop(Integer slop) {
        this.slop = slop;
        return this;
    }

    public MatchPhraseQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public MatchPhraseQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public String getField() {
        return field;
    }

    public Object getQuery() {
        return query;
    }

    public String getAnalyzer() {
        return analyzer;
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

    /*{
            "match_phrase" : {
                "message" : {
                    "query" : "this is a test"
                }
            }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("query", this.query);
        if (this.analyzer != null) {
            params.put("analyzer", this.analyzer);
        }
        if (this.slop != null) {
            params.put("slop", this.slop);
        }
        if (this.boost != null) {
            params.put("boost", this.boost);
        }
        if (this.queryName != null) {
            params.put("_name", this.queryName);
        }

        Map<String, Object> match = new LinkedHashMap<>();
        match.put(this.field, params);
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("match_phrase", match);
        return query;
    }
}
