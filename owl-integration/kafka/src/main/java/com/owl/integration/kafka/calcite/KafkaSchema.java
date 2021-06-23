package com.owl.integration.kafka.calcite;

import com.google.common.collect.ImmutableMap;
import com.owl.api.schema.IntegrationSchema;
import com.owl.integration.kafka.KafkaConfig;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class KafkaSchema extends AbstractSchema implements Closeable {
    private final IntegrationSchema integrationSchema = new IntegrationSchema();
    private final List<String> topics;
    private final KafkaConfig config;
    private final Map<String, Table> tableMap;

    public KafkaSchema(List<String> topics, KafkaConfig config) {
        this.topics = topics;
        this.config = config;
        this.tableMap = createTables();
    }

    public IntegrationSchema getIntegrationSchema() {
        return integrationSchema;
    }

    @Override
    protected Map<String, Table> getTableMap() {
        return tableMap;
    }

    private Map<String, Table> createTables() {
        final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
        for (String topic : topics) {
            KafkaStreamTable table = new KafkaStreamTable(topic, config);
            builder.put(topic, table);
            integrationSchema.addTable(table.getTableSchema());
        }
        return builder.build();
    }

    @Override
    public void close() throws IOException {
        for (Map.Entry<String, Table> entry : tableMap.entrySet()) {
            ((KafkaStreamTable) entry.getValue()).close();
        }
    }
}
