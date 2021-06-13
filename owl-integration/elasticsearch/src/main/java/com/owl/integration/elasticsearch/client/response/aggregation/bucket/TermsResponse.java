package com.owl.integration.elasticsearch.client.response.aggregation.bucket;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;

import java.io.IOException;

public class TermsResponse extends BucketResponse {
    private String key;

    public TermsResponse(SearchRequest search, String name) {
        super(search, name);
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getKeyAsString() {
        return key;
    }

    @Override
    public TermsResponse deserialize(JsonNode json) throws IOException {
        if(json.has(KEY)) {
            this.key = json.get(KEY).asText();
        }
        if(json.has(DOC_COUNT)) {
            this.docCount = json.get(DOC_COUNT).asLong();
        }
        if(json.has(AggregationResponse.META)) {
            this.meta = JsonUtil.decode2Map(json.get(AggregationResponse.META));
        }
        return this;
    }
}
