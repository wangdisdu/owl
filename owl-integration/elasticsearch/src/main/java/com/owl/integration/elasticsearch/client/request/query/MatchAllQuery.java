package com.owl.integration.elasticsearch.client.request.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-match-all-query.html
 */
public class MatchAllQuery extends Query {
    protected Float boost;
    protected String queryName;

    public MatchAllQuery setBoost(Float boost) {
        this.boost = boost;
        return this;
    }

    public MatchAllQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public Float getBoost() { return this.boost; }

    public String getQueryName() { return this.queryName; }

    /*{
        "match_all": {
            "boost": 1.2
        }
    }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        if(this.boost != null) params.put("boost", this.boost);
        if(this.queryName != null) params.put("_name", this.queryName);
        Map<String, Object> outline = new LinkedHashMap<>();
        outline.put("match_all", params);
        return outline;
    }
}
