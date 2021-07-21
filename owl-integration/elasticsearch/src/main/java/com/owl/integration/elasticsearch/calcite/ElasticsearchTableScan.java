package com.owl.integration.elasticsearch.calcite;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rel.rules.CoreRules;
import org.apache.calcite.rel.type.RelDataType;

import java.util.List;
import java.util.Objects;

public class ElasticsearchTableScan extends TableScan implements ElasticsearchRelNode {
    private final ElasticsearchTable elasticsearchTable;
    private final RelDataType projectRowType;

    ElasticsearchTableScan(RelOptCluster cluster,
                           RelOptTable table,
                           ElasticsearchTable elasticsearchTable,
                           RelDataType projectRowType) {
        super(cluster, cluster.traitSetOf(ElasticsearchRelNode.CONVENTION), ImmutableList.of(), table);
        this.elasticsearchTable = Objects.requireNonNull(elasticsearchTable, "elasticsearchTable");
        this.projectRowType = projectRowType;
    }

    @Override
    public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
        assert inputs.isEmpty();
        return this;
    }

    @Override
    public RelDataType deriveRowType() {
        return projectRowType != null ? projectRowType : super.deriveRowType();
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        final float f = projectRowType == null ? 1f : (float) projectRowType.getFieldCount() / 100f;
        return super.computeSelfCost(planner, mq).multiplyBy(.1 * f);
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
