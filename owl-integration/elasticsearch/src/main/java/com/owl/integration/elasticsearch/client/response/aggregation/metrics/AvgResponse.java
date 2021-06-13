package com.owl.integration.elasticsearch.client.response.aggregation.metrics;

import com.owl.integration.elasticsearch.client.request.SearchRequest;

public class AvgResponse extends SingleMetricResponse {
    public AvgResponse(SearchRequest search, String name) {
        super(search, name);
    }
}
