package com.owl.integration.elasticsearch.calcite;

import cn.hutool.core.collection.CollUtil;
import com.owl.api.reflect.ParamValueConverter;
import com.owl.api.schema.TableColumn;
import com.owl.api.schema.TableSchema;
import com.owl.integration.elasticsearch.client.IndexClient;
import com.owl.integration.elasticsearch.client.IndexMapping;
import com.owl.integration.elasticsearch.client.response.SearchResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.metrics.SingleMetricResponse;
import com.owl.integration.elasticsearch.client.response.hit.SearchHit;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.schema.TranslatableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElasticsearchTable extends AbstractTable implements TranslatableTable, Closeable {
    private final IndexClient indexClient;
    private final IndexMapping indexMapping;
    private final String indexName;
    private final TableSchema tableSchema = new TableSchema();

    public ElasticsearchTable(IndexClient indexClient) {
        this.indexClient = indexClient;
        this.indexMapping = indexClient.mappings();
        this.indexName = indexClient.getIndex();
        this.tableSchema.setName(indexName);
    }

    public TableSchema getTableSchema() {
        return tableSchema;
    }

    @Override
    public RelNode toRel(RelOptTable.ToRelContext context, RelOptTable relOptTable) {
        final RelOptCluster cluster = context.getCluster();
        return new ElasticsearchTableScan(cluster, relOptTable, this);
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory relDataTypeFactory) {
        RelDataTypeFactory.Builder builder = relDataTypeFactory.builder();
        for (Map.Entry<String, String> entry : indexMapping.getMapping().entrySet()) {
            SqlTypeName sqlTypeName = mapSqlTypeFromEsType(entry.getValue());
            RelDataType relDataType = relDataTypeFactory.createSqlType(sqlTypeName);
            builder.add(entry.getKey(), relDataType).nullable(true);
        }
        RelDataType rowType = builder.build();
        for (RelDataTypeField field : rowType.getFieldList()) {
            String name = field.getName();
            int jdbcType = field.getType().getSqlTypeName().getJdbcOrdinal();
            tableSchema.addColumn(TableColumn.build(name, jdbcType));
        }
        return rowType;
    }

    public List<Object[]> search(ElasticsearchRelNode.Implementor implementor) {
        List<Object[]> results = new ArrayList<>();
        SearchResponse response = indexClient.search(implementor.search);

        if(implementor.aggregated) {
            for (AggregationResponse bucket : response.getAggregations()) {
                List<Object> row = new ArrayList<>();
                row.add(bucket.getName());
                if(CollUtil.isNotEmpty(bucket.getAggregations())) {
                    for(AggregationResponse agg : bucket.getAggregations()) {
                        if(agg instanceof SingleMetricResponse) {
                            row.add(((SingleMetricResponse) agg).getValue());
                        }
                    }
                }
                results.add(row.toArray());
            }
        } else {
            Map<String, TableColumn> columns = new LinkedHashMap<>();
            for (TableColumn column : tableSchema.getColumns()) {
                columns.put(column.getName(), column);
            }
            for (SearchHit hit : response.getHits().getHits()) {
                List<Object> row = new ArrayList<>();
                Map<String, Object> source = hit.getSource();
                if(CollUtil.isEmpty(implementor.fieldMap)) {
                    tableSchema.getColumns().forEach(column -> {
                        row.add(ParamValueConverter.convert(
                                column.getJavaType(),
                                source.get(column.getName())
                        ));
                    });
                } else {
                    implementor.fieldMap.forEach((key, value) -> {
                        row.add(ParamValueConverter.convert(
                                columns.get(key).getJavaType(),
                                source.get(key)
                        ));
                    });
                }
                results.add(row.toArray());
            }
        }
        return results;
    }

    @Override
    public String toString() {
        return "ElasticsearchTable{" + indexName + "}";
    }

    private static SqlTypeName mapSqlTypeFromEsType(String type) {
        switch (type) {
            case "string": // for ES2
            case "text":
            case "keyword":
            case "date":
                return SqlTypeName.VARCHAR;
            case "long":
            case "integer":
            case "short":
            case "byte":
            case "float":
            case "double":
                return SqlTypeName.DOUBLE;
        }
        return SqlTypeName.VARCHAR;
    }

    @Override
    public void close() throws IOException {
        indexClient.close();
    }
}
