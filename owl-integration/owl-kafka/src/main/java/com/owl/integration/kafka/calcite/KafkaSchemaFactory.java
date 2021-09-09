package com.owl.integration.kafka.calcite;

import com.owl.integration.kafka.KafkaConfig;
import com.owl.integration.kafka.client.KafkaClientBuilder;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KafkaSchemaFactory implements SchemaFactory {

    @Override
    public KafkaSchema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        KafkaConfig config = new KafkaConfig();
        config.configure(operand);
        try {
            List<String> topics = listTopics(config);
            return new KafkaSchema(topics, config);
        } catch (Exception e) {
            throw new RuntimeException("Cannot build Kafka schema", e);
        }
    }

    private List<String> listTopics(KafkaConfig config) throws Exception {
        try (AdminClient adminClient = KafkaClientBuilder.createAdmin(config)) {
            ListTopicsResult result = adminClient.listTopics();
            Set<String> topics = result.names().get();
            return new ArrayList<>(topics);
        }
    }
}
