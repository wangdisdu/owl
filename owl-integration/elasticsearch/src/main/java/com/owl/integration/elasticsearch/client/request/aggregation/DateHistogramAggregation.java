package com.owl.integration.elasticsearch.client.request.aggregation;

import com.owl.integration.elasticsearch.client.request.script.Script;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-datehistogram-aggregation.html
 */
public class DateHistogramAggregation extends BucketAggregation {
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
    protected Object timeZone;
    protected String format;
    protected String offset;
    protected Boolean keyed;

    public DateHistogramAggregation setField(String field) {
        this.field = field;
        return this;
    }

    public DateHistogramAggregation setScript(Script script) {
        this.script = script;
        return this;
    }

    public DateHistogramAggregation setMissing(Object missing) {
        this.missing = missing;
        return this;
    }

    public DateHistogramAggregation addMeta(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }

    public DateHistogramAggregation setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public DateHistogramAggregation setInterval(Object interval) {
        this.interval = interval;
        return this;
    }

    // Order specifies the sort order. Valid values for order are:
    // "_key", "_count", a sub-aggregation name, or a sub-aggregation name with a metric.
    public DateHistogramAggregation setOrder(String order, Boolean asc) {
        // {
        //     "aggs" : {
        //         "sale_date" : {
        //             "date_histogram" : {
        //                 "field" : "date",
        //                 "interval": "hour",
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
    public DateHistogramAggregation setOrderByCount(Boolean asc) {
        this.order = "_count";
        this.orderAsc = asc;
        return this;
    }

    // "order" : { "_key" : "asc" }
    public DateHistogramAggregation setOrderByKey(Boolean asc) {
        this.order = "_key";
        this.orderAsc = asc;
        return this;
    }

    public DateHistogramAggregation setMinDocCount(Long minDocCount) {
        this.minDocCount = minDocCount;
        return this;
    }

    public DateHistogramAggregation setExtendedBounds(Object min, Object max) {
        this.extendedBoundsMin = min;
        this.extendedBoundsMax = max;
        return this;
    }

    public DateHistogramAggregation setExtendedBoundsMin(Object min) {
        this.extendedBoundsMin = min;
        return this;
    }

    public DateHistogramAggregation setExtendedBoundsMax(Object max) {
        this.extendedBoundsMax = max;
        return this;
    }

    public DateHistogramAggregation setTimeZone(Object timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public DateHistogramAggregation setFormat(String format) {
        this.format = format;
        return this;
    }

    public DateHistogramAggregation setOffset(String offset) {
        this.offset = offset;
        return this;
    }

    public DateHistogramAggregation setKeyed(Boolean keyed) {
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

    public Object getTimeZone() {
        return timeZone;
    }

    public String getFormat() {
        return format;
    }

    public String getOffset() {
        return offset;
    }

    public Boolean getKeyed() {
        return keyed;
    }

    /*{
                 "date_histogram" : { "field" : "date", "interval" : "month" }
            }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> opts = new LinkedHashMap<>();
        if (this.field != null) {
            opts.put("field", this.field);
        }
        if (this.interval != null) {
            opts.put("interval", this.interval);
        }
        if (this.script != null) {
            opts.put("script", this.script.serialize());
        }
        if (this.missing != null) {
            opts.put("missing", this.missing);
        }
        if (this.minDocCount != null) {
            opts.put("min_doc_count", this.minDocCount);
        }
        if (this.timeZone != null) {
            opts.put("time_zone", this.timeZone);
        }
        if (this.offset != null) {
            opts.put("offset", this.offset);
        }
        if (this.format != null) {
            opts.put("format", this.format);
        }
        if (this.keyed != null) {
            opts.put("keyed", this.keyed);
        }
        if (this.order != null) {
            Map<String, String> ord = new LinkedHashMap<>();
            ord.put(this.order, Boolean.TRUE.equals(this.orderAsc) ? "asc" : "desc");
            opts.put("order", ord);
        }
        if (this.extendedBoundsMax != null || this.extendedBoundsMin != null) {
            Map<String, Object> bounds = new LinkedHashMap<>();
            if (this.extendedBoundsMax != null) {
                bounds.put("max", this.extendedBoundsMax);
            }
            if (this.extendedBoundsMin != null) {
                bounds.put("min", this.extendedBoundsMin);
            }
            opts.put("extended_bounds", bounds);
        }
        Map<String, Object> source = new LinkedHashMap<>();
        if (this.meta.size() > 0) {
            source.put("meta", this.meta);
        }
        source.put("date_histogram", opts);
        if (this.subAggregations.size() > 0) {
            Map<String, Object> subAggs = new LinkedHashMap<>();
            for (Map.Entry<String, Aggregation> entry : this.subAggregations.entrySet()) {
                subAggs.put(entry.getKey(), entry.getValue().serialize());
            }
            source.put("aggregations", subAggs);
        }
        return source;
    }
}
