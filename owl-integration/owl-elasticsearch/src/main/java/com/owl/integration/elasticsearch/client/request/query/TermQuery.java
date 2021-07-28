package com.owl.integration.elasticsearch.client.request.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-term-query.html
 */
public class TermQuery extends Query {
    protected String field;
    protected Object value;
    protected Double boost;
    protected String queryName;

    public TermQuery setField(String field) {
        this.field = field;
        return this;
    }

    public TermQuery setTerm(Object value) {
        this.value = value;
        return this;
    }

    public TermQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public TermQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public Double getBoost() {
        return boost;
    }

    public String getQueryName() {
        return queryName;
    }

    /*{
            "term":{
                "name":"value"
            }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> term = new LinkedHashMap<>();
        if (this.queryName == null && this.boost == null) {
            term.put(this.field, this.value);
        } else {
            Map<String, Object> sub = new LinkedHashMap<>();
            sub.put("value", this.value);
            if (this.boost != null) {
                sub.put("boost", this.boost);
            }
            if (this.queryName != null) {
                sub.put("_name", this.queryName);
            }
            term.put(this.field, sub);
        }
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("term", term);
        return query;
    }
}
