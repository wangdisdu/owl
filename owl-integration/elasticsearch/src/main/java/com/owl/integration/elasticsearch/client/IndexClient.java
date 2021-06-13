package com.owl.integration.elasticsearch.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.response.SearchResponse;
import org.elasticsearch.client.Request;

import java.util.Map;

public class IndexClient {
    private final EsClient esClient;
    private final String index;

    public IndexClient(EsClient esClient, String index) {
        this.esClient = esClient;
        this.index = index;
    }

    public EsClient getEsClient() {
        return esClient;
    }

    public String getIndex() {
        return index;
    }

    public IndexMapping mappings() {
        return esClient.mappings(index);
    }

    public SearchResponse search(SearchRequest search) {
        Request request = new Request("POST", "/" + index + "/_search");
        try {
            Map<String, Object> dsl = search.build();
            String requestJson = JsonUtil.encode(dsl);
            request.setJsonEntity(requestJson);
            JsonNode responseJson = esClient.performRequest(request);
            SearchResponse response = new SearchResponse(search);
            response.deserialize(responseJson);
            return response;
        } catch (Exception e) {
            throw new EsCallException("failed request " + request.getEndpoint(), e);
        }
    }
}
