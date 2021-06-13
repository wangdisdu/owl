package com.owl.integration.elasticsearch.client.request.aggregation;

import com.owl.integration.elasticsearch.client.request.script.Script;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-histogram-aggregation.html
 */
public class HistogramAggregation extends BucketAggregation {
    protected String field;
    protected Script script;
    protected Object missing;
    protected Map<String, Object> meta = new LinkedHashMap<>();

    protected Object interval;
    protected String order;
    protected Boolean orderAsc;
    protected Long minDocCount;
    protected Object extendedBoundsMin;
    protected Object extendedBoundsMax;
    protected Object offset;
    protected Boolean keyed;

    public HistogramAggregation setField(String field) {
        this.field = field;
        return this;
    }

    public HistogramAggregation setScript(Script script) {
        this.script = script;
        return this;
    }

    public HistogramAggregation setMissing(Object missing) {
        this.missing = missing;
        return this;
    }

    public HistogramAggregation addMeta(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }
    public HistogramAggregation setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public HistogramAggregation setInterval(Object interval) {
        this.interval = interval;
        return this;
    }

    // Order specifies the sort order. Valid values for order are:
    // "_key", "_count", a sub-aggregation name, or a sub-aggregation name with a metric.
    public HistogramAggregation setOrder(String order, Boolean asc) {
        // {
        //     "aggs" : {
        //         "prices" : {
        //             "histogram" : {
        //                 "field" : "price",
        //                 "order" : { "avg_height" : "desc" }
        //             },
        //             "aggs" : {
        //                 "avg_height" : { "avg" : { "field" : "height" } }
        //             }
        //         }
        //     }
        // }
        this.order = order;
        this.orderAsc = asc;
        return this;
    }

    // "order" : { "_count" : "asc" }
    public HistogramAggregation setOrderByCount(Boolean asc) {
        this.order = "_count";
        this.orderAsc = asc;
        return this;
    }

    // "order" : { "_key" : "asc" }
    public HistogramAggregation setOrderByKey(Boolean asc) {
        this.order = "_key";
        this.orderAsc = asc;
        return this;
    }

    public HistogramAggregation setMinDocCount(Long minDocCount) {
        this.minDocCount = minDocCount;
        return this;
    }

    public HistogramAggregation setExtendedBounds(Object min, Object max) {
        this.extendedBoundsMin = min;
        this.extendedBoundsMax = max;
        return this;
    }

    public HistogramAggregation setExtendedBoundsMin(Object min) {
        this.extendedBoundsMin = min;
        return this;
    }

    public HistogramAggregation setExtendedBoundsMax(Object max) {
        this.extendedBoundsMax = max;
        return this;
    }

    public HistogramAggregation setOffset(Object offset) {
        this.offset = offset;
        return this;
    }

    public HistogramAggregation setKeyed(Boolean keyed) {
        this.keyed = keyed;
        return this;
    }

    public String getField() {
        return field;
    }

    public Script getScript() {
        return script;
    }

    public Object getMissing() {
        return missing;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public Object getInterval() {
        return interval;
    }

    public String getOrder() {
        return order;
    }

    public Boolean getOrderAsc() {
        return orderAsc;
    }

    public Long getMinDocCount() {
        return minDocCount;
    }

    public Object getExtendedBoundsMin() {
        return extendedBoundsMin;
    }

    public Object getExtendedBoundsMax() {
        return extendedBoundsMax;
    }

    public Object getOffset() {
        return offset;
    }

    public Boolean getKeyed() {
        return keyed;
    }

    /*{
            "histogram" : {
                "field" : "price",
                "interval" : 50
            }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> opts = new LinkedHashMap<>();
        if(this.field != null) opts.put("field", this.field);
        if(this.interval != null) opts.put("interval", this.interval);
        if(this.script != null) opts.put("script", this.script.serialize());
        if(this.missing != null) opts.put("missing", this.missing);
        if(this.minDocCount != null) opts.put("min_doc_count", this.minDocCount);
        if(this.offset != null) opts.put("offset", this.offset);
        if(this.keyed != null) opts.put("keyed", this.keyed);
        if(this.order != null) {
            Map<String, String> ord = new LinkedHashMap<>();
            ord.put(this.order, this.orderAsc ? "asc" : "desc");
            opts.put("order", ord);
        }
        if(this.extendedBoundsMax != null || this.extendedBoundsMin != null) {
            Map<String, Object> bounds = new LinkedHashMap<>();
            if(this.extendedBoundsMax != null) {
                bounds.put("max", this.extendedBoundsMax);
            }
            if(this.extendedBoundsMin != null) {
                bounds.put("min", this.extendedBoundsMin);
            }
            opts.put("extended_bounds", bounds);
        }
        Map<String, Object> source = new LinkedHashMap<>();
        if(this.meta.size() > 0) source.put("meta", this.meta);
        source.put("histogram", opts);
        if(this.subAggregations.size() > 0) {
            Map<String, Object> subAggs = new LinkedHashMap<>();
            for(Map.Entry<String, Aggregation> entry : this.subAggregations.entrySet()) {
                subAggs.put(entry.getKey(), entry.getValue().serialize());
            }
            source.put("aggregations", subAggs);
        }
        return source;
    }
}
