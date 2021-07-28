package com.owl.integration.elasticsearch.client.request.aggregation;

import com.owl.integration.elasticsearch.client.request.script.Script;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-range-aggregation.html
 */
public class RangeAggregation extends BucketAggregation {
    protected String field;
    protected Script script;
    protected Object missing;
    protected Map<String, Object> meta = new LinkedHashMap<>();

    protected List<Entry> entries = new ArrayList<>();
    protected Boolean keyed;

    public RangeAggregation setField(String field) {
        this.field = field;
        return this;
    }

    public RangeAggregation setScript(Script script) {
        this.script = script;
        return this;
    }

    public RangeAggregation setMissing(Object missing) {
        this.missing = missing;
        return this;
    }

    public RangeAggregation addMeta(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }

    public RangeAggregation setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public RangeAggregation setKeyed(Boolean keyed) {
        this.keyed = keyed;
        return this;
    }

    public RangeAggregation setFrom(Object from) {
        this.entries.add(new Entry(null, from, null));
        return this;
    }

    public RangeAggregation setFrom(String key, Object from) {
        this.entries.add(new Entry(key, from, null));
        return this;
    }

    public RangeAggregation setTo(Object to) {
        this.entries.add(new Entry(null, null, to));
        return this;
    }

    public RangeAggregation setTo(String key, Object to) {
        this.entries.add(new Entry(key, null, to));
        return this;
    }

    public RangeAggregation setBetween(Object from, Object to) {
        this.entries.add(new Entry(null, from, to));
        return this;
    }

    public RangeAggregation setBetween(String key, Object from, Object to) {
        this.entries.add(new Entry(key, from, to));
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

    public List<Entry> getEntries() {
        return entries;
    }

    public Boolean getKeyed() {
        return keyed;
    }

    /*{
            "range": {
                "field" : "price",
                "ranges": [
                    { "to" : 100.0 },
                    { "from" : 100.0, "to" : 200.0 },
                    { "from" : 200.0 }
                ]
            }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> opts = new LinkedHashMap<>();
        if (this.field != null) {
            opts.put("field", this.field);
        }
        if (this.script != null) {
            opts.put("script", this.script.serialize());
        }
        if (this.missing != null) {
            opts.put("missing", this.missing);
        }
        if (this.keyed != null) {
            opts.put("keyed", this.keyed);
        }
        List<Map<String, Object>> ranges = new ArrayList<>();
        for (Entry entry : this.entries) {
            Map<String, Object> ran = new LinkedHashMap<>();
            if (entry.key != null) {
                ran.put("key", entry.key);
            }
            if (entry.from != null) {
                ran.put("from", entry.from);
            }
            if (entry.to != null) {
                ran.put("to", entry.to);
            }
            ranges.add(ran);
        }
        opts.put("ranges", ranges);
        Map<String, Object> source = new LinkedHashMap<>();
        if (this.meta.size() > 0) {
            source.put("meta", this.meta);
        }
        source.put("range", opts);
        if (this.subAggregations.size() > 0) {
            Map<String, Object> subAggs = new LinkedHashMap<>();
            for (Map.Entry<String, Aggregation> entry : this.subAggregations.entrySet()) {
                subAggs.put(entry.getKey(), entry.getValue().serialize());
            }
            source.put("aggregations", subAggs);
        }
        return source;
    }

    public static class Entry {
        protected String key;
        protected Object from;
        protected Object to;

        public Entry(String key, Object from, Object to) {
            this.key = key;
            this.from = from;
            this.to = to;
        }
    }
}
