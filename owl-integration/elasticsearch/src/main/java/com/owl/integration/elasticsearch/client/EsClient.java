package com.owl.integration.elasticsearch.client;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EsClient implements Closeable {
    private final RestClient restClient;
    private EsVersion version;

    public EsClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void open() throws IOException {
        this.version = version();
    }

    public RestClient getRestClient() {
        return restClient;
    }

    public EsVersion getVersion() {
        return version;
    }

    public EsVersion version() {
        Request request = new Request("GET", "/");
        JsonNode response = performRequest(request);
        return EsVersion.fromString(response.get("version").get("number").asText());
    }

    public List<String> indices() {
        Request request = new Request("GET", "/_alias");
        JsonNode response = performRequest(request);
        return CollUtil.newArrayList(response.fieldNames());
    }

    public IndexMapping mappings(String index) {
        Request request = new Request("GET", "/" + index + "/_mappings");
        JsonNode response = performRequest(request);
        return IndexMapping.fromJson(version, index, response);
    }

    public JsonNode performRequest(Request request) {
        try {
            final Response response = restClient.performRequest(request);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode < 200 || statusCode >= 300) {
                throw new EsCallException("failed request " + request.getEndpoint() + " with response code " + statusCode);
            }
            try (InputStream is = response.getEntity().getContent()) {
                return JsonUtil.decode2Tree(is);
            }
        } catch (IOException e) {
            throw new EsCallException("failed request " + request.getEndpoint(), e);
        }
    }

    @Override
    public void close() throws IOException {
        if(restClient != null) {
            restClient.close();
        }
    }
}
