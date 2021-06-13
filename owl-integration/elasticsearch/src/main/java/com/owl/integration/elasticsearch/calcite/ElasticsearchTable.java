package com.owl.integration.elasticsearch.calcite;

import cn.hutool.core.collection.CollUtil;
import com.owl.api.annotation.DataType;
import com.owl.api.reflect.ParamValueConverter;
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
import org.apache.calcite.schema.TranslatableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElasticsearchTable extends AbstractTable implements TranslatableTable {
    private final IndexClient indexClient;
    private final IndexMapping indexMapping;
    private final String indexName;
    private Map<String, DataType> columns = new LinkedHashMap<>();

    public ElasticsearchTable(IndexClient indexClient) {
        this.indexClient = indexClient;
        this.indexMapping = indexClient.mappings();
        this.indexName = indexClient.getIndex();
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
            DataType dataType = mapDataTypeFromEsType(entry.getValue());
            columns.put(entry.getKey(), dataType);
        }
        return builder.build();
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
            for (SearchHit hit : response.getHits().getHits()) {
                List<Object> row = new ArrayList<>();
                Map<String, Object> source = hit.getSource();
                if(CollUtil.isEmpty(implementor.fieldMap)) {
                    columns.forEach((key, value) -> {
                        row.add(ParamValueConverter.convert(
                                value,
                                source.get(key)
                        ));
                    });
                } else {
                    implementor.fieldMap.forEach((key, value) -> {
                        row.add(ParamValueConverter.convert(
                                columns.get(key),
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

    private static DataType mapDataTypeFromEsType(String type) {
        switch (type) {
            case "string": // for ES2
            case "text":
            case "keyword":
            case "date":
                return DataType.STRING;
            case "long":
            case "integer":
            case "short":
            case "byte":
            case "float":
            case "double":
                return DataType.DOUBLE;
        }
        return DataType.STRING;
    }
}
