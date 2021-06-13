package com.owl.integration.elasticsearch.client.response.aggregation.bucket;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.EasyUtil;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;

import java.io.IOException;

public class HistogramResponse extends BucketResponse {
    private Double key;

    public HistogramResponse(SearchRequest search, String name) {
        super(search, name);
    }

    @Override
    public Double getKey() {
        return key;
    }

    @Override
    public String getKeyAsString() {
        return EasyUtil.formatDouble(key);
    }

    @Override
    public HistogramResponse deserialize(JsonNode json) throws IOException {
        if(json.has(KEY)) {
            this.key = json.get(KEY).asDouble();
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
