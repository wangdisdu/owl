package com.owl.integration.elasticsearch.client.response.aggregation.bucket;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;

import java.io.IOException;

public class DateHistogramResponse extends BucketResponse {
    private Double key;
    private String keyAsString;

    public DateHistogramResponse(SearchRequest search, String name) {
        super(search, name);
    }

    @Override
    public Double getKey() {
        return key;
    }

    @Override
    public String getKeyAsString() {
        return keyAsString;
    }

    @Override
    public DateHistogramResponse deserialize(JsonNode json) throws IOException {
        if (json.has(AggregationResponse.KEY)) {
            this.key = json.get(AggregationResponse.KEY).asDouble();
        }
        if (json.has(AggregationResponse.KEY_AS_STRING)) {
            this.keyAsString = json.get(AggregationResponse.KEY_AS_STRING).asText();
        }
        if (json.has(AggregationResponse.DOC_COUNT)) {
            this.docCount = json.get(AggregationResponse.DOC_COUNT).asLong();
        }
        if (json.has(AggregationResponse.META)) {
            this.meta = JsonUtil.decode2Map(json.get(AggregationResponse.META));
        }
        return this;
    }
}
