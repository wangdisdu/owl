package com.owl.integration.elasticsearch.client.request.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-range-query.html
 */
public class RangeQuery extends Query {
    protected String field;
    protected Object from;
    protected Object to;
    protected Boolean includeFrom;
    protected Boolean includeTo;
    protected String timeZone;
    protected String format;
    protected Double boost;
    protected String queryName;

    public RangeQuery setField(String field) {
        this.field = field;
        return this;
    }

    public RangeQuery setGT(Object from) {
        this.from = from;
        this.includeFrom = false;
        return this;
    }

    public RangeQuery setGTE(Object from) {
        this.from = from;
        this.includeFrom = true;
        return this;
    }

    public RangeQuery setFrom(Object from, Boolean include) {
        this.from = from;
        this.includeFrom = include;
        return this;
    }

    public RangeQuery setLT(Object to) {
        this.to = to;
        this.includeTo = false;
        return this;
    }

    public RangeQuery setLTE(Object to) {
        this.to = to;
        this.includeTo = true;
        return this;
    }

    public RangeQuery setTo(Object to, Boolean include) {
        this.to = to;
        this.includeTo = include;
        return this;
    }

    public RangeQuery setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public RangeQuery setFormat(String format) {
        this.format = format;
        return this;
    }

    public RangeQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public RangeQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public String getField() {
        return field;
    }

    public Object getFrom() {
        return from;
    }

    public Object getTo() {
        return to;
    }

    public Boolean getIncludeFrom() {
        return includeFrom;
    }

    public Boolean getIncludeTo() {
        return includeTo;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getFormat() {
        return format;
    }

    public Double getBoost() {
        return boost;
    }

    public String getQueryName() {
        return queryName;
    }

    /*{
            "range" : {
                "age" : {
                    "gte" : 10,
                    "lte" : 20
                }
            }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        if(this.from != null) {
            if(this.includeFrom != null && this.includeFrom) {
                params.put("gte", this.from);
            } else {
                params.put("gt", this.from);
            }
        }
        if(this.to != null) {
            if(this.includeTo != null && this.includeTo) {
                params.put("lte", this.to);
            } else {
                params.put("lt", this.to);
            }
        }
        if(this.timeZone != null){
            params.put("time_zone", this.timeZone);
        }
        if(this.format != null) params.put("format", this.format);
        if(this.boost != null) params.put("boost", this.boost);
        if(this.queryName != null) params.put("_name", this.queryName);

        Map<String, Object> range = new LinkedHashMap<>();
        range.put(this.field, params);
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("range", range);
        return query;
    }
}
