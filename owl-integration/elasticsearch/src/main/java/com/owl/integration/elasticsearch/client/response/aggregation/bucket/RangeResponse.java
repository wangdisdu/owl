package com.owl.integration.elasticsearch.client.response.aggregation.bucket;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.EasyUtil;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;

import java.io.IOException;

public class RangeResponse extends BucketResponse {
    private Double from;
    private Double to;

    public RangeResponse(SearchRequest search, String name) {
        super(search, name);
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    @Override
    public String getKey() {
        String fromString = from == null ? "*" : EasyUtil.formatDouble(from);
        String toString = to == null ? "*" : EasyUtil.formatDouble(to);
        return fromString + "-" + toString;
    }

    @Override
    public String getKeyAsString() {
        return getKey();
    }

    @Override
    public RangeResponse deserialize(JsonNode json) throws IOException {
        if(json.has(AggregationResponse.FROM)) {
            this.from = json.get(AggregationResponse.FROM).asDouble();
        }
        if(json.has(AggregationResponse.TO)) {
            this.to = json.get(AggregationResponse.TO).asDouble();
        }
        if(json.has(AggregationResponse.DOC_COUNT)) {
            this.docCount = json.get(AggregationResponse.DOC_COUNT).asLong();
        }
        if(json.has(AggregationResponse.META)) {
            this.meta = JsonUtil.decode2Map(json.get(AggregationResponse.META));
        }
        return this;
    }
}
