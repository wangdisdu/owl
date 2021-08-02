package com.owl.integration.elasticsearch.client;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.Constants;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        Request request = new Request(Constants.GET, "/");
        JsonNode response = performRequest(request);
        return EsVersion.fromString(response.get("version").get("number").asText());
    }

    public List<String> indices() {
        Request request = new Request(Constants.GET, "/_alias");
        JsonNode response = performRequest(request);
        Iterator<Map.Entry<String, JsonNode>> iterator = response.fields();
        HashSet<String> indices = new HashSet<>();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String index = entry.getKey();
            indices.add(index);
            Iterator<String> aliases = entry.getValue().get("aliases").fieldNames();
            indices.addAll(CollUtil.newArrayList(aliases));
        }
        return CollUtil.newArrayList(indices);
    }

    public IndexMapping mappings(String index) {
        Request request = new Request(Constants.GET, "/" + index + "/_mappings");
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
        if (restClient != null) {
            restClient.close();
        }
    }
}
