package com.owl.integration.elasticsearch.client.response.aggregation.metrics;

import com.owl.integration.elasticsearch.client.request.SearchRequest;

public class MinResponse extends SingleMetricResponse {
    public MinResponse(SearchRequest search, String name) {
        super(search, name);
    }
}