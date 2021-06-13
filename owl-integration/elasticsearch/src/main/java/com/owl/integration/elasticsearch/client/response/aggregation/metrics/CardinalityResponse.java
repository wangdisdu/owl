package com.owl.integration.elasticsearch.client.response.aggregation.metrics;

import com.owl.integration.elasticsearch.client.request.SearchRequest;

public class CardinalityResponse extends SingleMetricResponse {
    public CardinalityResponse(SearchRequest search, String name) {
        super(search, name);
    }
}
