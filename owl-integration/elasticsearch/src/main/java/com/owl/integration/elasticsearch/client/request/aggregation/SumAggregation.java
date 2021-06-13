package com.owl.integration.elasticsearch.client.request.aggregation;

import com.owl.integration.elasticsearch.client.request.script.Script;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-sum-aggregation.html
 */
public class SumAggregation extends MetricAggregation {
    protected String field;
    protected Script script;
    protected String format;
    protected Object missing;
    protected Map<String, Object> meta = new LinkedHashMap<>();

    public SumAggregation setField(String field) {
        this.field = field;
        return this;
    }

    public SumAggregation setScript(Script script) {
        this.script = script;
        return this;
    }

    public SumAggregation setFormat(String format) {
        this.format = format;
        return this;
    }

    public SumAggregation setMissing(Object missing) {
        this.missing = missing;
        return this;
    }

    public SumAggregation setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public SumAggregation addMeta(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }

    public String getField() {
        return field;
    }

    public Script getScript() {
        return script;
    }

    public String getFormat() {
        return format;
    }

    public Object getMissing() {
        return missing;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    /*{
            "sum" : { "field" : "grade" }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> opts = new LinkedHashMap<>();
        if(this.field != null) opts.put("field", this.field);
        if(this.script != null) opts.put("script", this.script.serialize());
        if(this.format != null) opts.put("format", this.format);
        if(this.missing != null) opts.put("missing", this.missing);
        Map<String, Object> source = new LinkedHashMap<>();
        if(this.meta.size() > 0) source.put("meta", this.meta);
        source.put("sum", opts);
        return source;
    }
}
