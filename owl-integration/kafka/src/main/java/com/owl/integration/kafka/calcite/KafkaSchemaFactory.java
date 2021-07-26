package com.owl.integration.kafka.calcite;

import com.owl.integration.kafka.KafkaConfig;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class KafkaSchemaFactory implements SchemaFactory {

    @Override
    public KafkaSchema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        KafkaConfig config = new KafkaConfig();
        config.configure(operand);
        try {
            Properties props = new Properties();
            props.setProperty("bootstrap.servers", config.getBootstrapServers());
            List<String> topics = listTopics(props);
            return new KafkaSchema(topics, config);
        } catch (Exception e) {
            throw new RuntimeException("Cannot build Kafka schema", e);
        }
    }

    private List<String> listTopics(Properties props) throws Exception {
        try (AdminClient adminClient = KafkaAdminClient.create(props)) {
            ListTopicsResult result = adminClient.listTopics();
            Set<String> topics = result.names().get();
            return new ArrayList<>(topics);
        }
    }
}
