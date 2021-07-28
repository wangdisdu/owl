package com.owl.integration.elasticsearch.client.response.aggregation.metrics;

import com.owl.integration.elasticsearch.client.request.SearchRequest;

public class MaxResponse extends SingleMetricResponse {
    public MaxResponse(SearchRequest search, String name) {
        super(search, name);
    }
}
