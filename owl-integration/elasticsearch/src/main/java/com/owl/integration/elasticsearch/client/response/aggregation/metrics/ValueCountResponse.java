package com.owl.integration.elasticsearch.client.response.aggregation.metrics;

import com.owl.integration.elasticsearch.client.request.SearchRequest;

public class ValueCountResponse extends SingleMetricResponse {
    public ValueCountResponse(SearchRequest search, String name) {
        super(search, name);
    }
}
