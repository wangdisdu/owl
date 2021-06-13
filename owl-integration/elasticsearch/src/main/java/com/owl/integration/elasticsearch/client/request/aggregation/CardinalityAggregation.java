package com.owl.integration.elasticsearch.client.request.aggregation;

import com.owl.integration.elasticsearch.client.request.script.Script;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-cardinality-aggregation.html
 */
public class CardinalityAggregation extends MetricAggregation {
    protected String field;
    protected Script script;
    protected String format;
    protected Object missing;
    protected Long precisionThreshold;
    protected Map<String, Object> meta = new LinkedHashMap<>();

    public CardinalityAggregation setField(String field) {
        this.field = field;
        return this;
    }

    public CardinalityAggregation setScript(Script script) {
        this.script = script;
        return this;
    }

    public CardinalityAggregation setFormat(String format) {
        this.format = format;
        return this;
    }

    public CardinalityAggregation setMissing(Object missing) {
        this.missing = missing;
        return this;
    }

    public CardinalityAggregation setPrecisionThreshold(Long precisionThreshold) {
        this.precisionThreshold = precisionThreshold;
        return this;
    }

    public CardinalityAggregation setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public CardinalityAggregation addMeta(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }

    /*{
        "cardinality" : { "field" : "author" }
    }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> opts = new LinkedHashMap<>();
        if(this.field != null) opts.put("field", this.field);
        if(this.script != null) opts.put("script", this.script.serialize());
        if(this.format != null) opts.put("format", this.format);
        if(this.missing != null) opts.put("missing", this.missing);
        if(this.precisionThreshold != null) opts.put("precision_threshold", this.precisionThreshold);
        Map<String, Object> source = new LinkedHashMap<>();
        if(this.meta.size() > 0) source.put("meta", this.meta);
        source.put("cardinality", opts);
        return source;
    }
}
