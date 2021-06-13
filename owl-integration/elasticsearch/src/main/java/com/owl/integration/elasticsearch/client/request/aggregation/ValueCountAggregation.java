package com.owl.integration.elasticsearch.client.request.aggregation;

import com.owl.integration.elasticsearch.client.request.script.Script;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-valuecount-aggregation.html
 */
public class ValueCountAggregation extends MetricAggregation {
    protected String field;
    protected Script script;
    protected String format;
    protected Map<String, Object> meta = new LinkedHashMap<>();

    public ValueCountAggregation setField(String field) {
        this.field = field;
        return this;
    }

    public ValueCountAggregation setScript(Script script) {
        this.script = script;
        return this;
    }

    public ValueCountAggregation setFormat(String format) {
        this.format = format;
        return this;
    }

    public ValueCountAggregation setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public ValueCountAggregation addMeta(String key, Object value) {
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

    public Map<String, Object> getMeta() {
        return meta;
    }

    /*{
            "value_count" : { "field" : "grade" }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> opts = new LinkedHashMap<>();
        if(this.field != null) opts.put("field", this.field);
        if(this.script != null) opts.put("script", this.script.serialize());
        if(this.format != null) opts.put("format", this.format);
        Map<String, Object> source = new LinkedHashMap<>();
        if(this.meta.size() > 0) source.put("meta", this.meta);
        source.put("value_count", opts);
        return source;
    }
}
