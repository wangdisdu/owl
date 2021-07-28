package com.owl.integration.elasticsearch.client.response.hit;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.integration.elasticsearch.client.ResponseDeserializer;
import com.owl.integration.elasticsearch.client.request.SearchRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchHits implements ResponseDeserializer {
    public static final String TOTAL = "total";
    public static final String VALUE = "value";
    public static final String MAX_SCORE = "max_score";
    public static final String HITS = "hits";

    protected SearchRequest search;

    protected Long total;
    protected Double maxScore;
    protected List<SearchHit> hits;

    public SearchHits(SearchRequest search) {
        this.search = search;
    }

    public SearchRequest getSearch() {
        return search;
    }

    public Long getTotal() {
        return total;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public List<SearchHit> getHits() {
        return hits;
    }

    @Override
    public SearchHits deserialize(JsonNode json) throws IOException {
        if (json.has(MAX_SCORE)) {
            this.maxScore = json.get(MAX_SCORE).asDouble();
        }
        if (json.get(TOTAL).isObject()) {
            this.total = json.get(TOTAL).get(VALUE).asLong();
        } else {
            this.total = json.get(TOTAL).asLong();
        }
        if (json.has(HITS)) {
            List<SearchHit> list = new ArrayList<>();
            JsonNode array = json.get(HITS);
            for (JsonNode item : array) {
                SearchHit hit = new SearchHit(search);
                hit.deserialize(item);
                list.add(hit);
            }
            this.hits = list;
        }
        return this;
    }
}
