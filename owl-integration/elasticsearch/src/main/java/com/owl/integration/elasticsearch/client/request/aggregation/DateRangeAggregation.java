package com.owl.integration.elasticsearch.client.request.aggregation;

import com.owl.integration.elasticsearch.client.request.script.Script;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-daterange-aggregation.html
 */
public class DateRangeAggregation extends BucketAggregation {
    protected String field;
    protected Script script;
    protected Object missing;
    protected Map<String, Object> meta = new LinkedHashMap<>();

    protected List<Entry> entries = new ArrayList<>();
    protected Object timeZone;
    protected String format;
    protected Boolean keyed;

    public DateRangeAggregation setField(String field) {
        this.field = field;
        return this;
    }

    public DateRangeAggregation setScript(Script script) {
        this.script = script;
        return this;
    }

    public DateRangeAggregation setMissing(Object missing) {
        this.missing = missing;
        return this;
    }

    public DateRangeAggregation setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public DateRangeAggregation addMeta(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }

    public DateRangeAggregation setTimeZone(Object timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public DateRangeAggregation setFormat(String format) {
        this.format = format;
        return this;
    }

    public DateRangeAggregation setKeyed(Boolean keyed) {
        this.keyed = keyed;
        return this;
    }

    public DateRangeAggregation setFrom(Object from) {
        this.entries.add(new Entry(null, from, null));
        return this;
    }

    public DateRangeAggregation setFrom(String key, Object from) {
        this.entries.add(new Entry(key, from, null));
        return this;
    }

    public DateRangeAggregation setTo(Object to) {
        this.entries.add(new Entry(null, null, to));
        return this;
    }

    public DateRangeAggregation setTo(String key, Object to) {
        this.entries.add(new Entry(key, null, to));
        return this;
    }

    public DateRangeAggregation setBetween(Object from, Object to) {
        this.entries.add(new Entry(null, from, to));
        return this;
    }

    public DateRangeAggregation setBetween(String key, Object from, Object to) {
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

    public Object getTimeZone() {
        return timeZone;
    }

    public String getFormat() {
        return format;
    }

    public Boolean getKeyed() {
        return keyed;
    }

    /*{
            "date_range": {
                "field": "date",
                "format": "MM-yyy",
                "ranges": [
                    { "to": "now-10M/M" },
                    { "from": "now-10M/M" }
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
        if (this.timeZone != null) {
            opts.put("time_zone", this.timeZone);
        }
        if (this.format != null) {
            opts.put("format", this.format);
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
        source.put("date_range", opts);
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
