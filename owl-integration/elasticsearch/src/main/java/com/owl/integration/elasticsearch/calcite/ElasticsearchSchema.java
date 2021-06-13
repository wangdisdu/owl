package com.owl.integration.elasticsearch.calcite;

import com.google.common.collect.ImmutableMap;
import com.owl.integration.elasticsearch.client.EsClient;
import com.owl.integration.elasticsearch.client.IndexClient;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.List;
import java.util.Map;

public class ElasticsearchSchema extends AbstractSchema {
    private final EsClient esClient;
    private final Map<String, Table> tableMap;

    public ElasticsearchSchema(EsClient esClient) {
        this.esClient = esClient;
        this.tableMap = createTables();
    }

    @Override
    public Map<String, Table> getTableMap() {
        return tableMap;
    }

    private Map<String, Table> createTables() {
        List<String> indices = esClient.indices();
        final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
        for (String index : indices) {
            final IndexClient indexClient = new IndexClient(esClient, index);
            builder.put(index, new ElasticsearchTable(indexClient));
        }
        return builder.build();
    }
}
