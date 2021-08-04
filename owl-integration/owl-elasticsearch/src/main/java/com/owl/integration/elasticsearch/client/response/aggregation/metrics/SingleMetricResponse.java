package com.owl.integration.elasticsearch.client.response.aggregation.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;

import java.io.IOException;

public class SingleMetricResponse extends AggregationResponse {
    protected Object value;

    public SingleMetricResponse(SearchRequest search, String name) {
        super(search, name);
    }

    public Object getValue() {
        return value;
    }

    public Object parseValue(JsonNode value) {
        return value.asDouble();
    }

    @Override
    public SingleMetricResponse deserialize(JsonNode json) throws IOException {
        if (json.has(VALUE)) {
            this.value = parseValue(json.get(VALUE));
        }
        if (json.has(AggregationResponse.META)) {
            this.meta = JsonUtil.decode2Map(json.get(AggregationResponse.META));
        }
        return this;
    }
}
