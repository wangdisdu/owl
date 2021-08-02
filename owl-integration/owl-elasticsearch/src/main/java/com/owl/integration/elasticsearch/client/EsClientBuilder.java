package com.owl.integration.elasticsearch.client;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Strings;
import com.owl.integration.elasticsearch.ElasticsearchConfig;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.net.ssl.SSLContext;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

public class EsClientBuilder {
    /**
     * Builds Elastic rest client from user configuration
     * initialized low-level rest http client for ES
     */
    public static EsClient build(ElasticsearchConfig config) throws IOException {
        List<HttpHost> hosts = StrUtil.split(config.getHosts(), ',')
                .stream()
                .map(HttpHost::create)
                .collect(Collectors.toList());

        RestClientBuilder builder = RestClient.builder(hosts.toArray(new HttpHost[hosts.size()]));
        if (!Strings.isNullOrEmpty(config.getPathPrefix())) {
            builder.setPathPrefix(config.getPathPrefix());
        }

        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setSSLContext(buildSSLContext());
            httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
            if (!Strings.isNullOrEmpty(config.getUsername()) && !Strings.isNullOrEmpty(config.getPassword())) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(
                        AuthScope.ANY,
                        new UsernamePasswordCredentials(config.getUsername(), config.getPassword()));
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
            return httpClientBuilder;
        });

        EsClient esClient = new EsClient(builder.build());
        esClient.open();
        return esClient;
    }

    private static SSLContext buildSSLContext() {
        try {
            TrustStrategy trustAnyStrategy = new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            };
            return SSLContexts.custom().loadTrustMaterial(null, trustAnyStrategy).build();
        } catch (Exception e) {
            throw new IORuntimeException(e);
        }
    }
}
