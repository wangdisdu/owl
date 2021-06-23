package com.owl.integration.elasticsearch.calcite;

import com.google.common.collect.ImmutableMap;
import com.owl.api.schema.IntegrationSchema;
import com.owl.integration.elasticsearch.client.EsClient;
import com.owl.integration.elasticsearch.client.IndexClient;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ElasticsearchSchema extends AbstractSchema implements Closeable {
    private final IntegrationSchema integrationSchema = new IntegrationSchema();
    private final EsClient esClient;
    private final Map<String, Table> tableMap;

    public ElasticsearchSchema(EsClient esClient) {
        this.esClient = esClient;
        this.tableMap = createTables();
    }

    public IntegrationSchema getIntegrationSchema() {
        return integrationSchema;
    }

    @Override
    public Map<String, Table> getTableMap() {
        return tableMap;
    }

    private Map<String, Table> createTables() {
        List<String> indices = esClient.indices();
        ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
        for (String index : indices) {
            IndexClient indexClient = new IndexClient(esClient, index);
            ElasticsearchTable table = new ElasticsearchTable(index, indexClient);
            builder.put(index, table);
            integrationSchema.addTable(table.getTableSchema());
        }
        return builder.build();
    }

    @Override
    public void close() throws IOException {
        for (Map.Entry<String, Table> entry : tableMap.entrySet()) {
            ((ElasticsearchTable) entry.getValue()).close();
        }
        esClient.close();
    }
}
