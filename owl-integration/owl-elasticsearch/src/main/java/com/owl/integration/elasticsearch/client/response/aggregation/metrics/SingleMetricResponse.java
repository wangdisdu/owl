package com.owl.integration.elasticsearch.client.response.aggregation.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;

import java.io.IOException;

public class SingleMetricResponse extends AggregationResponse {
    protected Double value;

    public SingleMetricResponse(SearchRequest search, String name) {
        super(search, name);
    }

    public Double getValue() {
        return value;
    }

    @Override
    public SingleMetricResponse deserialize(JsonNode json) throws IOException {
        if (json.has(VALUE)) {
            this.value = json.get(VALUE).asDouble();
        }
        if (json.has(AggregationResponse.META)) {
            this.meta = JsonUtil.decode2Map(json.get(AggregationResponse.META));
        }
        return this;
    }
}
