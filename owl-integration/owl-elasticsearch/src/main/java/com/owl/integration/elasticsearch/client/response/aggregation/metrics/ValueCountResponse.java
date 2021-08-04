package com.owl.integration.elasticsearch.client.response.aggregation.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.integration.elasticsearch.client.request.SearchRequest;

public class ValueCountResponse extends SingleMetricResponse {
    public ValueCountResponse(SearchRequest search, String name) {
        super(search, name);
    }

    @Override
    public Object parseValue(JsonNode value) {
        return value.asLong();
    }
}
