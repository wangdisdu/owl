package com.owl.integration.elasticsearch.client.response.aggregation;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.integration.elasticsearch.client.ResponseDeserializer;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.request.aggregation.Aggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.AvgAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.BucketAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.CardinalityAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.DateHistogramAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.DateRangeAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.HistogramAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.MaxAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.MetricAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.MinAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.RangeAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.SumAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.TermsAggregation;
import com.owl.integration.elasticsearch.client.request.aggregation.ValueCountAggregation;
import com.owl.integration.elasticsearch.client.response.aggregation.bucket.DateHistogramResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.bucket.DateRangeResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.bucket.HistogramResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.bucket.RangeResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.bucket.TermsResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.metrics.AvgResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.metrics.CardinalityResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.metrics.MaxResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.metrics.MinResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.metrics.SumResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.metrics.ValueCountResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AggregationsResponse implements ResponseDeserializer {
    public static final String BUCKETS = "buckets";

    protected SearchRequest search;
    protected List<AggregationResponse> aggregations;

    public AggregationsResponse(SearchRequest search) {
        this.search = search;
    }

    public List<AggregationResponse> getAggregations() {
        return aggregations;
    }

    @Override
    public AggregationsResponse deserialize(JsonNode json) throws IOException {
        this.aggregations = deserialize(json, search.getAggregations());
        return this;
    }

    private List<AggregationResponse> deserialize(JsonNode json, Map<String, Aggregation> aggs) throws IOException {
        List<AggregationResponse> list = new ArrayList<>();
        if (aggs == null || aggs.size() == 0) {
            return list;
        }
        for (Map.Entry<String, Aggregation> entry : aggs.entrySet()) {
            String name = entry.getKey();
            JsonNode node = json.get(name);
            if (node == null) {
                continue;
            }
            Aggregation agg = entry.getValue();
            if (agg instanceof MetricAggregation) {
                MetricAggregation magg = (MetricAggregation)agg;
                if (magg instanceof AvgAggregation) {
                    AvgResponse resp = new AvgResponse(search, name);
                    resp.deserialize(node);
                    list.add(resp);
                } else if (magg instanceof CardinalityAggregation) {
                    CardinalityResponse resp = new CardinalityResponse(search, name);
                    resp.deserialize(node);
                    list.add(resp);
                } else if (magg instanceof MaxAggregation) {
                    MaxResponse resp = new MaxResponse(search, name);
                    resp.deserialize(node);
                    list.add(resp);
                } else if (magg instanceof MinAggregation) {
                    MinResponse resp = new MinResponse(search, name);
                    resp.deserialize(node);
                    list.add(resp);
                } else if (magg instanceof SumAggregation) {
                    SumResponse resp = new SumResponse(search, name);
                    resp.deserialize(node);
                    list.add(resp);
                } else if (magg instanceof ValueCountAggregation) {
                    ValueCountResponse resp = new ValueCountResponse(search, name);
                    resp.deserialize(node);
                    list.add(resp);
                }
                // TODO
            } else if (agg instanceof BucketAggregation) {
                BucketAggregation bagg = (BucketAggregation) agg;
                if (bagg instanceof DateHistogramAggregation) {
                    JsonNode array = node.get(BUCKETS);
                    if (array == null) {
                        continue;
                    }
                    for (JsonNode bucket : array) {
                        DateHistogramResponse resp = new DateHistogramResponse(search, name);
                        resp.deserialize(bucket);
                        resp.setAggregations(deserialize(bucket, bagg.getSubAggregations()));
                        list.add(resp);
                    }
                } else if (bagg instanceof DateRangeAggregation) {
                    JsonNode array = node.get(BUCKETS);
                    if (array == null) {
                        continue;
                    }
                    for (JsonNode bucket : array) {
                        DateRangeResponse resp = new DateRangeResponse(search, name);
                        resp.deserialize(bucket);
                        resp.setAggregations(deserialize(bucket, bagg.getSubAggregations()));
                        list.add(resp);
                    }
                } else if (bagg instanceof HistogramAggregation) {
                    JsonNode array = node.get(BUCKETS);
                    if (array == null) {
                        continue;
                    }
                    for (JsonNode bucket : array) {
                        HistogramResponse resp = new HistogramResponse(search, name);
                        resp.deserialize(bucket);
                        resp.setAggregations(deserialize(bucket, bagg.getSubAggregations()));
                        list.add(resp);
                    }
                } else if (bagg instanceof RangeAggregation) {
                    JsonNode array = node.get(BUCKETS);
                    if (array == null) {
                        continue;
                    }
                    for (JsonNode bucket : array) {
                        RangeResponse resp = new RangeResponse(search, name);
                        resp.deserialize(bucket);
                        resp.setAggregations(deserialize(bucket, bagg.getSubAggregations()));
                        list.add(resp);
                    }
                } else if (bagg instanceof TermsAggregation) {
                    JsonNode array = node.get(BUCKETS);
                    if (array == null) {
                        continue;
                    }
                    for (JsonNode bucket : array) {
                        TermsResponse resp = new TermsResponse(search, name);
                        resp.deserialize(bucket);
                        resp.setAggregations(deserialize(bucket, bagg.getSubAggregations()));
                        list.add(resp);
                    }
                }
                // TODO
            }
        }
        return list;
    }
}
