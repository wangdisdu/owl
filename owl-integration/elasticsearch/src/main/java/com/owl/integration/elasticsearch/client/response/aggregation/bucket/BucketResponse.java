package com.owl.integration.elasticsearch.client.response.aggregation.bucket;

import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;

public abstract class BucketResponse extends AggregationResponse {
    protected Long docCount;

    public BucketResponse(SearchRequest search, String name) {
        super(search, name);
    }

    public Long getDocCount() {
        return docCount;
    }

    public abstract Object getKey();

    public abstract String getKeyAsString();
}
