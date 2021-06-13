package com.owl.integration.elasticsearch.calcite;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.owl.integration.elasticsearch.ElasticsearchConfig;
import com.owl.integration.elasticsearch.client.EsClient;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ElasticsearchSchemaFactory implements SchemaFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchSchemaFactory.class);

    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        ElasticsearchConfig config = new ElasticsearchConfig();
        config.configure(operand);
        try {
            List<HttpHost> hosts = StrUtil.split(config.getHosts(), ',')
                    .stream()
                    .map(HttpHost::create)
                    .collect(Collectors.toList());
            final EsClient client = connect(hosts, config.getPathPrefix(), config.getUsername(), config.getPassword());
            return new ElasticsearchSchema(client);
        } catch (Exception e) {
            throw new RuntimeException("Cannot build elasticsearch schema", e);
        }
    }

    /**
     * Builds Elastic rest client from user configuration.
     *
     * @param hosts list of ES HTTP Hosts to connect to
     * @param username the username of ES
     * @param password the password of ES
     * @return newly initialized low-level rest http client for ES
     */
    private static EsClient connect(List<HttpHost> hosts,
                                    String pathPrefix,
                                    String username,
                                    String password) throws IOException {

        Objects.requireNonNull(hosts, "hosts or coordinates");
        Preconditions.checkArgument(!hosts.isEmpty(), "no ES hosts specified");

        RestClientBuilder builder = RestClient.builder(hosts.toArray(new HttpHost[hosts.size()]));

        if (!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(password)) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));
            builder.setHttpClientConfigCallback(httpClientBuilder ->
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }

        if (pathPrefix != null && !pathPrefix.isEmpty()) {
            builder.setPathPrefix(pathPrefix);
        }
        EsClient esClient = new EsClient(builder.build());
        esClient.open();
        return esClient;
    }
}
