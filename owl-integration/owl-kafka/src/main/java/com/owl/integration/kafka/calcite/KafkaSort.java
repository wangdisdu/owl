package com.owl.integration.kafka.calcite;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Sort;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;

public class KafkaSort extends Sort implements KafkaRelNode {

    public KafkaSort(RelOptCluster cluster,
                     RelTraitSet traits,
                     RelNode child,
                     RelCollation collation,
                     RexNode offset,
                     RexNode fetch) {
        super(cluster, traits, child, collation, offset, fetch);
        assert !this.collation.getFieldCollations().isEmpty() : "Sort not supported";
    }

    @Override
    public Sort copy(RelTraitSet traitSet, RelNode relNode, RelCollation relCollation,
                     RexNode offset, RexNode fetch) {
        return new KafkaSort(getCluster(), traitSet, relNode, collation, offset, fetch);
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq).multiplyBy(0.05);
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        if (offset != null) {
            throw new UnsupportedOperationException("offset not supported");
        }
        if (fetch != null) {
            implementor.setLimit(((RexLiteral) fetch).getValueAs(Long.class));
        }
    }

}
