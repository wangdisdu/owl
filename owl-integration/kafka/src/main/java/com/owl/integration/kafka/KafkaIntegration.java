package com.owl.integration.kafka;

import com.owl.api.IntegrationBuilder;
import com.owl.api.IntegrationContext;
import com.owl.api.annotation.Integration;

import java.sql.Connection;
import java.util.Map;

@Integration(
        display = "Kafka",
        description = "Kafka Integration"
)
public class KafkaIntegration implements IntegrationBuilder<KafkaConfig> {

    @Override
    public void build(IntegrationContext context, KafkaConfig config) throws Exception {

    }

    @Override
    public KafkaConfig configure(Map<String, Object> map) {
        return null;
    }

    @Override
    public Connection connect() throws Exception {
        return null;
    }
}
