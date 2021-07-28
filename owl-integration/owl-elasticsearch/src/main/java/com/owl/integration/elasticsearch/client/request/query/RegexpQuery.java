package com.owl.integration.elasticsearch.client.request.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-regexp-query.html
 */
public class RegexpQuery extends Query {
    protected String field;
    protected String regexp;
    protected String flags;
    protected Integer maxDeterminizedStates;
    protected Double boost;
    protected String queryName;

    public RegexpQuery setField(String field) {
        this.field = field;
        return this;
    }

    public RegexpQuery setRegexp(String regexp) {
        this.regexp = regexp;
        return this;
    }

    public RegexpQuery setFlags(String flags) {
        this.flags = flags;
        return this;
    }

    public RegexpQuery setMaxDeterminizedStates(Integer maxDeterminizedStates) {
        this.maxDeterminizedStates = maxDeterminizedStates;
        return this;
    }

    public RegexpQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public RegexpQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public String getField() {
        return field;
    }

    public String getRegexp() {
        return regexp;
    }

    public String getFlags() {
        return flags;
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
            "regexp":{
                "user": {
                    "value": "s.*y",
                    "flags" : "INTERSECTION|COMPLEMENT|EMPTY"
                }
            }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        if (this.boost == null && this.queryName == null && this.flags == null && this.maxDeterminizedStates == null) {
            params.put(this.field, this.regexp);
        } else {
            Map<String, Object> sub = new LinkedHashMap<>();
            sub.put("value", this.regexp);
            if (this.flags != null) {
                sub.put("flags", this.flags);
            }
            if (this.maxDeterminizedStates != null) {
                sub.put("max_determinized_states", this.maxDeterminizedStates);
            }
            if (this.boost != null) {
                sub.put("boost", this.boost);
            }
            if (this.queryName != null) {
                sub.put("_name", this.queryName);
            }
            params.put(this.field, sub);
        }
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("regexp", params);
        return query;
    }
}
