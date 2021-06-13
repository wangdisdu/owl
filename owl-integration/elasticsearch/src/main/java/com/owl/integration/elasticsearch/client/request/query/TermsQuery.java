package com.owl.integration.elasticsearch.client.request.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-terms-query.html
 */
public class TermsQuery extends Query {
    protected String field;
    protected List<Object> values = new ArrayList<>();
    protected Double boost;
    protected String queryName;

    public TermsQuery setField(String field) {
        this.field = field;
        return this;
    }

    public TermsQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public TermsQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public TermsQuery setTerms(List<Object> values) {
        this.values = values;
        return this;
    }

    public TermsQuery addTerm(Object value) {
        this.values.add(value);
        return this;
    }

    public String getField() {
        return field;
    }

    public List<Object> getValues() {
        return values;
    }

    public Double getBoost() {
        return boost;
    }

    public String getQueryName() {
        return queryName;
    }

    /*{
            "terms":{
                "name":["kimchy", "elasticsearch"]
            }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> term = new LinkedHashMap<>();
        if(this.queryName == null && this.boost == null) {
            term.put(this.field, this.values);
        } else {
            Map<String, Object> sub = new LinkedHashMap<>();
            sub.put("value", this.values);
            if(this.boost != null) sub.put("boost", this.boost);
            if(this.queryName != null) sub.put("_name", this.queryName);
            term.put(this.field, sub);
        }
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("terms", term);
        return query;
    }
}
