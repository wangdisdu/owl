package com.owl.integration.elasticsearch.client.request.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-wildcard-query.html
 */
public class WildcardQuery extends Query {
    protected String field;
    protected String wildcard;
    protected Double boost;
    protected String queryName;

    public WildcardQuery setField(String field) {
        this.field = field;
        return this;
    }

    public WildcardQuery setWildcard(String wildcard) {
        this.wildcard = wildcard;
        return this;
    }

    public WildcardQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public WildcardQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public String getField() {
        return field;
    }

    public String getWildcard() {
        return wildcard;
    }

    public Double getBoost() {
        return boost;
    }

    public String getQueryName() {
        return queryName;
    }

    /*{
            "wildcard" : { "user" : { "value" : "ki*y", "boost" : 2.0 } }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        if(this.boost == null && this.queryName == null) {
            params.put(this.field, this.wildcard);
        } else {
            Map<String, Object> sub = new LinkedHashMap<>();
            sub.put("value", this.wildcard);
            if(this.boost != null) sub.put("boost", this.boost);
            if(this.queryName != null) sub.put("_name", this.queryName);
            params.put(this.field, sub);
        }
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("wildcard", params);
        return query;
    }
}
