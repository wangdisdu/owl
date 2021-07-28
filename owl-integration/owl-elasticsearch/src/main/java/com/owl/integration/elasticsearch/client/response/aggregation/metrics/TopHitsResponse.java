package com.owl.integration.elasticsearch.client.response.aggregation.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;
import com.owl.integration.elasticsearch.client.response.hit.SearchHits;

import java.io.IOException;

public class TopHitsResponse extends AggregationResponse {
    protected SearchHits hits;

    public TopHitsResponse(SearchRequest search, String name) {
        super(search, name);
    }

    public SearchHits getHits() {
        return hits;
    }

    @Override
    public TopHitsResponse deserialize(JsonNode json) throws IOException {
        if (json.has(SearchHits.HITS)) {
            this.hits = new SearchHits(search).deserialize(json.get(SearchHits.HITS));
        }
        if (json.has(AggregationResponse.META)) {
            this.meta = JsonUtil.decode2Map(json.get(AggregationResponse.META));
        }
        return this;
    }
}
