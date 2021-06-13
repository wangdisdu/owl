package com.owl.integration.elasticsearch.client.response.aggregation.metrics;

import com.owl.integration.elasticsearch.client.request.SearchRequest;

public class SumResponse extends SingleMetricResponse {
    public SumResponse(SearchRequest search, String name) {
        super(search, name);
    }
}