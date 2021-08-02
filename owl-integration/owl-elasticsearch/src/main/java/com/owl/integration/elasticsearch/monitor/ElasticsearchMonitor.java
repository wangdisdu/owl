package com.owl.integration.elasticsearch.monitor;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.integration.elasticsearch.Constants;
import com.owl.integration.elasticsearch.ElasticsearchConfig;
import com.owl.integration.elasticsearch.client.EsClient;
import com.owl.integration.elasticsearch.client.EsClientBuilder;
import org.elasticsearch.client.Request;

import java.io.Closeable;
import java.io.IOException;

public class ElasticsearchMonitor implements Closeable {
    private final EsClient client;

    public ElasticsearchMonitor(ElasticsearchConfig config) throws IOException {
        this(EsClientBuilder.build(config));
    }

    public ElasticsearchMonitor(EsClient client) {
        this.client = client;
    }

    public ClusterStats clusterStats() {
        Request request = new Request(Constants.GET, "/_cluster/stats");
        JsonNode response = client.performRequest(request);
        return ClusterStats.fromJson(response);
    }

    public NodeStats[] nodeStats() {
        Request request = new Request(Constants.GET, "/_nodes/stats");
        JsonNode response = client.performRequest(request);
        return NodeStats.fromJson(response);
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }
}
