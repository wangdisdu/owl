package com.owl.integration.elasticsearch.calcite;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.rules.CoreRules;

import java.util.Objects;

public class ElasticsearchTableScan extends TableScan implements ElasticsearchRelNode {
    private final ElasticsearchTable elasticsearchTable;

    ElasticsearchTableScan(RelOptCluster cluster,
                           RelOptTable table,
                           ElasticsearchTable elasticsearchTable) {
        super(cluster, cluster.traitSetOf(ElasticsearchRelNode.CONVENTION), ImmutableList.of(), table);
        this.elasticsearchTable = Objects.requireNonNull(elasticsearchTable, "elasticsearchTable");
    }

    @Override
    public void register(RelOptPlanner planner) {
        for (RelOptRule rule: ElasticsearchRules.RULES) {
            planner.addRule(rule);
        }
        // remove this rule otherwise elastic can't correctly interpret approx_count_distinct()
        // it is converted to cardinality aggregation in Elastic
        planner.removeRule(CoreRules.AGGREGATE_EXPAND_DISTINCT_AGGREGATES);
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.table = table;
        implementor.elasticsearchTable = elasticsearchTable;
    }
}
