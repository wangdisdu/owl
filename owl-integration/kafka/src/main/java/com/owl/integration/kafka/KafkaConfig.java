package com.owl.integration.kafka;

import com.owl.api.IntegrationConfig;
import com.owl.api.SmartConfig;
import com.owl.api.annotation.KeyValue;
import com.owl.api.annotation.Parameter;

public class KafkaConfig extends SmartConfig implements IntegrationConfig {
    @Parameter(
            display = "地址",
            placeholder = "host1:9092,host2:9092"
    )
    private String bootstrapServers;

    @Parameter(
            display = "Consumer配置",
            candidates = {"bytes", "string", "json"},
            required = false
    )
    private KeyValue consumerProperties;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public KeyValue getConsumerProperties() {
        return consumerProperties;
    }

    public void setConsumerProperties(KeyValue consumerProperties) {
        this.consumerProperties = consumerProperties;
    }
}
