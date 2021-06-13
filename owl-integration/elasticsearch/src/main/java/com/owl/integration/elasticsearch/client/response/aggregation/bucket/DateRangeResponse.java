package com.owl.integration.elasticsearch.client.response.aggregation.bucket;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;

import java.io.IOException;

public class DateRangeResponse extends BucketResponse {
    private Double from;
    private String fromAsString;
    private Double to;
    private String toAsString;

    public DateRangeResponse(SearchRequest search, String name) {
        super(search, name);
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public String getFromAsString() {
        return fromAsString;
    }

    public String getToAsString() {
        return toAsString;
    }

    @Override
    public String getKey() {
        String fromString = fromAsString == null || fromAsString.length() == 0 ? "*" : fromAsString;
        String toString = toAsString == null || toAsString.length() == 0 ? "*" : toAsString;
        return fromString + "-" + toString;
    }

    @Override
    public String getKeyAsString() {
        return getKey();
    }

    @Override
    public DateRangeResponse deserialize(JsonNode json) throws IOException {
        if(json.has(FROM)) {
            this.from = json.get(FROM).asDouble();
        }
        if(json.has(FROM_AS_STRING)) {
            this.fromAsString = json.get(FROM_AS_STRING).asText();
        }
        if(json.has(TO)) {
            this.to = json.get(TO).asDouble();
        }
        if(json.has(TO_AS_STRING)) {
            this.toAsString = json.get(TO_AS_STRING).asText();
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
