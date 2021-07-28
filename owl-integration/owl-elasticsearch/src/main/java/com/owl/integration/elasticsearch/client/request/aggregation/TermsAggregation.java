package com.owl.integration.elasticsearch.client.request.aggregation;

import com.owl.integration.elasticsearch.client.request.script.Script;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-terms-aggregation.html
 */
public class TermsAggregation extends BucketAggregation {
    protected String field;
    protected Script script;
    protected Object missing;
    protected Map<String, Object> meta = new LinkedHashMap<>();

    protected Integer size;
    protected Integer shardSize;
    protected Integer minDocCount;
    protected Integer shardMinDocCount;
    protected Boolean showTermDocCountError;
    protected String executionHint;
    protected String collectionMode;
    protected List<String> includeValues = new ArrayList<>();
    protected List<String> excludeValues = new ArrayList<>();
    protected Integer includePartition;
    protected Integer includeNumPartitions;
    protected List<Order> orders = new ArrayList<>();

    public TermsAggregation setField(String field) {
        this.field = field;
        return this;
    }

    public TermsAggregation setScript(Script script) {
        this.script = script;
        return this;
    }

    public TermsAggregation setMissing(Object missing) {
        this.missing = missing;
        return this;
    }

    public TermsAggregation addMeta(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }

    public TermsAggregation setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public TermsAggregation setSize(Integer size) {
        this.size = size;
        return this;
    }

    public TermsAggregation setShardSize(Integer shardSize) {
        this.shardSize = shardSize;
        return this;
    }

    public TermsAggregation setMinDocCount(Integer minDocCount) {
        this.minDocCount = minDocCount;
        return this;
    }

    public TermsAggregation setShardMinDocCount(Integer shardMinDocCount) {
        this.shardMinDocCount = shardMinDocCount;
        return this;
    }

    public TermsAggregation setShowTermDocCountError(Boolean showTermDocCountError) {
        this.showTermDocCountError = showTermDocCountError;
        return this;
    }

    public TermsAggregation setExecutionHint(String executionHint) {
        this.executionHint = executionHint;
        return this;
    }

    public TermsAggregation setCollectionMode(String collectionMode) {
        this.collectionMode = collectionMode;
        return this;
    }

    public TermsAggregation setIncludeValues(List<String> includeValues) {
        this.includeValues = includeValues;
        return this;
    }

    public TermsAggregation setExcludeValues(List<String> excludeValues) {
        this.excludeValues = excludeValues;
        return this;
    }

    public TermsAggregation setIncludePartition(Integer includePartition) {
        this.includePartition = includePartition;
        return this;
    }

    public TermsAggregation setIncludeNumPartitions(Integer includeNumPartitions) {
        this.includeNumPartitions = includeNumPartitions;
        return this;
    }

    // Order specifies the sort order. Valid values for order are:
    // "_key", "_count", a sub-aggregation name, or a sub-aggregation name with a metric.
    public TermsAggregation setOrder(String order, Boolean asc) {
        // {
        //     "aggs" : {
        //         "genders" : {
        //             "terms" : {
        //                 "field" : "gender",
        //                 "order" : { "avg_height" : "desc" }
        //             },
        //             "aggs" : {
        //                 "avg_height" : { "avg" : { "field" : "height" } }
        //             }
        //         }
        //     }
        // }
        this.orders.add(new Order(order, asc));
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

    public Integer getSize() {
        return size;
    }

    public Integer getShardSize() {
        return shardSize;
    }

    public Integer getMinDocCount() {
        return minDocCount;
    }

    public Integer getShardMinDocCount() {
        return shardMinDocCount;
    }

    public Boolean getShowTermDocCountError() {
        return showTermDocCountError;
    }

    public String getExecutionHint() {
        return executionHint;
    }

    public String getCollectionMode() {
        return collectionMode;
    }

    public List<String> getIncludeValues() {
        return includeValues;
    }

    public List<String> getExcludeValues() {
        return excludeValues;
    }

    public Integer getIncludePartition() {
        return includePartition;
    }

    public Integer getIncludeNumPartitions() {
        return includeNumPartitions;
    }

    public List<Order> getOrders() {
        return orders;
    }

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
        if (this.size != null) {
            opts.put("size", this.size);
        }
        if (this.shardSize != null) {
            opts.put("shard_size", this.shardSize);
        }
        if (this.minDocCount != null) {
            opts.put("min_doc_count", this.minDocCount);
        }
        if (this.shardMinDocCount != null) {
            opts.put("shard_min_doc_count", this.shardMinDocCount);
        }
        if (this.showTermDocCountError != null) {
            opts.put("show_term_doc_count_error", this.showTermDocCountError);
        }
        if (this.executionHint != null) {
            opts.put("execution_hint", this.executionHint);
        }
        if (this.collectionMode != null) {
            opts.put("collect_mode", this.collectionMode);
        }
        // Include
        if (this.includeNumPartitions != null && this.includeNumPartitions > 0) {
            Map<String, Integer> inc = new LinkedHashMap<>();
            inc.put("partition", this.includePartition);
            inc.put("num_partitions", this.includeNumPartitions);
            opts.put("include", inc);
        } else if (this.includeValues != null && this.includeValues.size() > 0) {
            if (this.includeValues.size() == 1) {
                opts.put("include", this.includeValues.get(0));
            } else {
                opts.put("include", this.includeValues);
            }
        }
        // Exclude
        if (this.excludeValues != null && this.excludeValues.size() > 0) {
            if (this.includeValues.size() == 1) {
                opts.put("exclude", this.excludeValues.get(0));
            } else {
                opts.put("exclude", this.excludeValues);
            }
        }
        // Order
        if (this.orders.size() == 1) {
            Order o = this.orders.get(0);
            Map<String, String> ord = new LinkedHashMap<>();
            ord.put(o.field, o.ascending ? "asc" : "desc");
            opts.put("order", ord);
        } else  if (this.orders.size() > 1) {
            List<Map<String, String>> ords = new ArrayList<>();
            for (Order o : this.orders) {
                Map<String, String> ord = new LinkedHashMap<>();
                ord.put(o.field, o.ascending ? "asc" : "desc");
                ords.add(ord);
            }
            opts.put("order", ords);
        }
        Map<String, Object> source = new LinkedHashMap<>();
        if (this.meta.size() > 0) {
            source.put("meta", this.meta);
        }
        source.put("terms", opts);
        if (this.subAggregations.size() > 0) {
            Map<String, Object> subAggs = new LinkedHashMap<>();
            for (Map.Entry<String, Aggregation> entry : this.subAggregations.entrySet()) {
                subAggs.put(entry.getKey(), entry.getValue().serialize());
            }
            source.put("aggregations", subAggs);
        }
        return source;
    }

    public static class Order {
        protected String field;
        protected Boolean ascending;

        public Order(String field, Boolean ascending) {
            this.field = field;
            this.ascending = ascending;
        }
    }

}
