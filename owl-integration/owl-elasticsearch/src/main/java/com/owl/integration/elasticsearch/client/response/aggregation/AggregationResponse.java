package com.owl.integration.elasticsearch.client.response.aggregation;

import com.owl.integration.elasticsearch.client.ResponseDeserializer;
import com.owl.integration.elasticsearch.client.request.SearchRequest;

import java.util.List;
import java.util.Map;

public abstract class AggregationResponse implements ResponseDeserializer {
    public static final String META = "meta";
    public static final String VALUE = "value";
    public static final String VALUES = "values";
    public static final String VALUE_AS_STRING = "value_as_string";
    public static final String DOC_COUNT = "doc_count";
    public static final String KEY = "key";
    public static final String KEY_AS_STRING = "key_as_string";
    public static final String FROM = "from";
    public static final String FROM_AS_STRING = "from_as_string";
    public static final String TO = "to";
    public static final String TO_AS_STRING = "to_as_string";

    protected SearchRequest search;
    protected String name;
    protected Map<String, Object> meta;
    protected List<AggregationResponse> aggregations;

    public AggregationResponse(SearchRequest search, String name) {
        this.search = search;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public List<AggregationResponse> getAggregations() {
        return aggregations;
    }

    public AggregationResponse setAggregations(List<AggregationResponse> aggregations) {
        this.aggregations = aggregations;
        return this;
    }
}
