package com.owl.integration.elasticsearch.calcite;

import com.owl.integration.elasticsearch.client.request.SearchRequest;
import com.owl.integration.elasticsearch.client.request.aggregation.Aggregation;
import com.owl.integration.elasticsearch.client.request.query.Query;
import com.owl.integration.elasticsearch.client.request.sorter.Sorter;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ElasticsearchRelNode extends RelNode {

    void implement(Implementor implementor);

    /**
     * Calling convention for relational operations that occur in Elasticsearch.
     */
    Convention CONVENTION = new Convention.Impl("ELASTICSEARCH", ElasticsearchRelNode.class);

    default List<String> fieldNames(RelDataType relDataType) {
        List<String> names = new ArrayList<>();
        for (RelDataTypeField field : relDataType.getFieldList()) {
            names.add(field.getName());
        }
        return names;
    }

    /**
     * Callback for the implementation process that converts a tree of
     * {@link ElasticsearchRelNode} nodes into an Elasticsearch query.
     */
    class Implementor {

        void visitChild(int ordinal, RelNode input) {
            assert ordinal == 0;
            ((ElasticsearchRelNode) input).implement(this);
        }

        final SearchRequest search = new SearchRequest();
        final Map<String, String> fieldMap = new LinkedHashMap<>();
        boolean aggregated = false;

        RelOptTable table;
        ElasticsearchTable elasticsearchTable;

        void setQuery(Query query) {
            search.setQuery(query);
        }

        void setAggregation(Map<String, Aggregation> aggregations) {
            search.setAggregations(aggregations);
            search.setFrom(0);
            search.setSize(0);
            aggregated = true;
        }

        void addField(String field, String alias) {
            fieldMap.put(field, alias);
            search.addIncludeField(field);
        }

        void addSort(Sorter sorter) {
            search.addSort(sorter);
        }

        void setOffset(int offset) {
            if (offset >= 0) {
                search.setFrom(offset);
            }
        }

        void setFetch(int fetch) {
            if (fetch >= 0) {
                search.setSize(fetch);
            }
        }
    }
}
