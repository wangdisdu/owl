package com.owl.integration.kafka.calcite;

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

import java.util.List;

public class KafkaTableScan extends TableScan implements KafkaRelNode {
    private final KafkaTable kafkaTable;

    protected KafkaTableScan(RelOptCluster cluster,
                             RelOptTable table,
                             KafkaTable kafkaTable) {
        super(cluster, cluster.traitSetOf(KafkaRelNode.CONVENTION), ImmutableList.of(), table);
        this.kafkaTable = kafkaTable;
    }

    @Override
    public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
        assert inputs.isEmpty();
        return this;
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq).multiplyBy(0.1);
    }

    @Override
    public void register(RelOptPlanner planner) {
        for (RelOptRule rule: KafkaRules.RULES) {
            planner.addRule(rule);
        }
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.kafkaTable = kafkaTable;
    }
}
