package com.owl.integration.elasticsearch.client.request.aggregation;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BucketAggregation extends Aggregation {

    protected Map<String, Aggregation> subAggregations = new LinkedHashMap<>();

    public BucketAggregation addSubAggregation(String name, Aggregation subAggregation) {
        this.subAggregations.put(name, subAggregation);
        return this;
    }

    public BucketAggregation setSubAggregations(Map<String, Aggregation> subAggregations) {
        this.subAggregations = subAggregations;
        return this;
    }

    public Map<String, Aggregation> getSubAggregations() {
        return subAggregations;
    }
}
