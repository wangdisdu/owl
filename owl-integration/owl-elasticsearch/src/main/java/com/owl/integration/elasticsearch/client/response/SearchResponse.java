package com.owl.integration.elasticsearch.client.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.ResponseDeserializer;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationsResponse;
import com.owl.integration.elasticsearch.client.response.hit.SearchHits;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SearchResponse implements ResponseDeserializer {
    public static final String ERROR = "error";
    public static final String TOOK = "took";
    public static final String TIMED_OUT = "timed_out";
    public static final String SCROLL_ID = "_scroll_id";
    public static final String SHARDS = "_shards";
    public static final String HITS = "hits";
    public static final String AGGREGATIONS = "aggregations";

    protected SearchRequest search;

    protected Map<String, Object> error;
    protected String errorString;
    protected Long took;
    protected Boolean timedOut;
    protected String scrollId;
    protected SearchHits hits;
    protected List<AggregationResponse> aggregations;

    public SearchResponse(SearchRequest search) {
        this.search = search;
    }

    public SearchRequest getSearch() {
        return search;
    }

    public Map<String, Object> getError() {
        return error;
    }

    public String getErrorString() {
        return errorString;
    }

    public Long getTook() {
        return took;
    }

    public Boolean getTimedOut() {
        return timedOut;
    }

    public String getScrollId() {
        return scrollId;
    }

    public SearchHits getHits() {
        return hits;
    }

    public List<AggregationResponse> getAggregations() {
        return aggregations;
    }

    public boolean hasError() {
        return this.error != null;
    }

    @Override
    public SearchResponse deserialize(JsonNode json) throws IOException {
        if (json.has(ERROR)) {
            this.error = JsonUtil.decode2Map(json.get(ERROR));
            this.errorString = json.get(ERROR).asText();
        }
        if (json.has(TOOK)) {
            this.took = json.get(TOOK).asLong();
        }
        if (json.has(TIMED_OUT)) {
            this.timedOut = json.get(TIMED_OUT).asBoolean();
        }
        if (json.has(SCROLL_ID)) {
            this.scrollId = json.get(SCROLL_ID).asText();
        }
        if (json.has(HITS)) {
            SearchHits hits = new SearchHits(search);
            hits.deserialize(json.get(HITS));
            this.hits = hits;
        }
        if (json.has(AGGREGATIONS)) {
            AggregationsResponse aggregations = new AggregationsResponse(search);
            aggregations.deserialize(json.get(AGGREGATIONS));
            this.aggregations = aggregations.getAggregations();
        }
        return this;
    }
}
