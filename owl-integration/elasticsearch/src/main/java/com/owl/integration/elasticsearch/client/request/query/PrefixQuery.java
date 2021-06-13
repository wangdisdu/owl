package com.owl.integration.elasticsearch.client.request.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-prefix-query.html
 */
public class PrefixQuery extends Query {
    protected String field;
    protected String prefix;
    protected Double boost;
    protected String queryName;

    public PrefixQuery setField(String field) {
        this.field = field;
        return this;
    }

    public PrefixQuery setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public PrefixQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public PrefixQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public String getField() {
        return field;
    }

    public String getPrefix() {
        return prefix;
    }

    public Double getBoost() {
        return boost;
    }

    public String getQueryName() {
        return queryName;
    }

    /*{
            "prefix" : { "user" :  { "value" : "ki", "boost" : 2.0 } }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        if(this.boost == null && this.queryName == null) {
            params.put(this.field, this.prefix);
        } else {
            Map<String, Object> sub = new LinkedHashMap<>();
            sub.put("value", this.prefix);
            if(this.boost != null) sub.put("boost", this.boost);
            if(this.queryName != null) sub.put("_name", this.queryName);
            params.put(this.field, sub);
        }
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("prefix", params);
        return query;
    }
}
