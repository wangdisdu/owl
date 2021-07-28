package com.owl.integration.elasticsearch.calcite;

import cn.hutool.core.collection.CollUtil;
import com.owl.api.reflect.ParamValueConverter;
import com.owl.api.schema.TableColumn;
import com.owl.api.schema.TableSchema;
import com.owl.integration.elasticsearch.client.IndexClient;
import com.owl.integration.elasticsearch.client.IndexMapping;
import com.owl.integration.elasticsearch.client.response.SearchResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.AggregationResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.bucket.BucketResponse;
import com.owl.integration.elasticsearch.client.response.aggregation.metrics.SingleMetricResponse;
import com.owl.integration.elasticsearch.client.response.hit.SearchHit;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.TranslatableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElasticsearchTable extends AbstractTable implements TranslatableTable, Closeable {
    private final String indexName;
    private final IndexClient indexClient;
    private final TableSchema tableSchema;

    public ElasticsearchTable(String indexName, IndexClient indexClient) {
        this.indexName = indexName;
        this.indexClient = indexClient;
        this.tableSchema = createTableSchema(indexName, indexClient.mappings());
    }

    public TableSchema getTableSchema() {
        return tableSchema;
    }

    private TableSchema createTableSchema(String index, IndexMapping mapping) {
        final TableSchema tableSchema = new TableSchema();
        tableSchema.setName(index);
        mapping.getMapping().forEach((key, value) ->
                tableSchema.addColumn(TableColumn.build(
                        key,
                        mapSqlTypeFromEsType(value)
                ))
        );
        return tableSchema;
    }

    private int mapSqlTypeFromEsType(String type) {
        switch (type) {
            case "string": // for ES2
            case "text":
            case "keyword":
            case "date":
                return Types.VARCHAR;
            case "long":
            case "integer":
            case "short":
            case "byte":
            case "float":
            case "double":
                return Types.DOUBLE;
            default:
                return Types.VARCHAR;
        }
    }

    @Override
    public RelNode toRel(RelOptTable.ToRelContext context, RelOptTable relOptTable) {
        final RelOptCluster cluster = context.getCluster();
        return new ElasticsearchTableScan(cluster, relOptTable, this, null);
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory relDataTypeFactory) {
        final RelDataTypeFactory.Builder builder = relDataTypeFactory.builder();
        for (TableColumn column : tableSchema.getColumns()) {
            SqlTypeName sqlTypeName = SqlTypeName.getNameForJdbcType(column.getJdbcType().code);
            RelDataType relDataType = relDataTypeFactory.createSqlType(sqlTypeName);
            builder.add(column.getName(), relDataType).nullable(true);
        }
        return builder.build();
    }

    public List<Object> search(ElasticsearchRelNode.Implementor implementor) {
        SearchResponse response = indexClient.search(implementor.search);
        if (implementor.aggregated) {
            return aggEnumerable(implementor, response);
        } else {
            return hitEnumerable(implementor, response);
        }
    }

    private List<Object> hitEnumerable(ElasticsearchRelNode.Implementor implementor, SearchResponse response) {
        List<Object> results = new ArrayList<>();
        Map<String, TableColumn> columns = new LinkedHashMap<>();
        for (TableColumn column : tableSchema.getColumns()) {
            columns.put(column.getName(), column);
        }
        for (SearchHit hit : response.getHits().getHits()) {
            List<Object> row = new ArrayList<>();
            Map<String, Object> source = hit.getSource();
            if (CollUtil.isEmpty(implementor.fieldMap)) {
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
        return results;
    }

    private List<Object> aggEnumerable(ElasticsearchRelNode.Implementor implementor, SearchResponse response) {
        if (CollUtil.isEmpty(response.getAggregations())) {
            return new ArrayList<>();
        }
        List<AggregationResponse> aggregations = response.getAggregations();
        boolean isOne = aggregations.size() == 1 && aggregations.get(0) instanceof SingleMetricResponse;
        if (isOne) {
            List<Object> results = new ArrayList<>();
            results.add(((SingleMetricResponse) response.getAggregations().get(0)).getValue());
            return results;
        }
        return aggEnumerable(implementor, aggregations);
    }

    private List<Object> aggEnumerable(ElasticsearchRelNode.Implementor implementor, List<AggregationResponse> aggregations) {
        List<Object> results = new ArrayList<>();
        boolean isBucket = CollUtil.isNotEmpty(aggregations) && aggregations.get(0) instanceof BucketResponse;
        boolean isMetric = CollUtil.isNotEmpty(aggregations) && aggregations.get(0) instanceof SingleMetricResponse;
        if (isMetric) {
            List<Object> row = new ArrayList<>();
            for (AggregationResponse agg : aggregations) {
                row.add(((SingleMetricResponse) agg).getValue());
            }
            results.add(row.toArray());
        } else if (isBucket) {
            for (AggregationResponse agg : aggregations) {
                if (CollUtil.isNotEmpty(agg.getAggregations())) {
                    List<Object> subResults = aggEnumerable(implementor, agg.getAggregations());
                    for (Object subResult : subResults) {
                        List<Object> row = new ArrayList<>();
                        row.add(((BucketResponse) agg).getKeyAsString());
                        if (subResult.getClass().isArray()) {
                            Object[] subArray = (Object[]) subResult;
                            row.addAll(Arrays.asList(subArray));
                        } else {
                            //todo not support
                        }
                        results.add(row.toArray());
                    }
                } else {
                    //todo not support
                }
            }
        }
        return results;
    }

    @Override
    public String toString() {
        return "ElasticsearchTable{" + indexName + "}";
    }

    @Override
    public void close() throws IOException {
        indexClient.close();
    }
}
