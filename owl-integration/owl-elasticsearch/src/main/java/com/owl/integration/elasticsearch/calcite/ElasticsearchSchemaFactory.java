package com.owl.integration.elasticsearch.calcite;

import com.owl.integration.elasticsearch.ElasticsearchConfig;
import com.owl.integration.elasticsearch.client.EsClient;
import com.owl.integration.elasticsearch.client.EsClientBuilder;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.Map;

public class ElasticsearchSchemaFactory implements SchemaFactory {

    @Override
    public ElasticsearchSchema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        ElasticsearchConfig config = new ElasticsearchConfig();
        config.configure(operand);
        try {
            final EsClient client = EsClientBuilder.build(config);
            return new ElasticsearchSchema(client);
        } catch (Exception e) {
            throw new RuntimeException("Cannot build elasticsearch schema", e);
        }
    }
}
