package com.owl.integration.kafka.calcite;

import org.apache.calcite.adapter.enumerable.EnumerableRel;
import org.apache.calcite.adapter.enumerable.EnumerableRelImplementor;
import org.apache.calcite.adapter.enumerable.JavaRowFormat;
import org.apache.calcite.adapter.enumerable.PhysType;
import org.apache.calcite.adapter.enumerable.PhysTypeImpl;
import org.apache.calcite.linq4j.tree.BlockStatement;
import org.apache.calcite.linq4j.tree.Blocks;
import org.apache.calcite.linq4j.tree.Expressions;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterImpl;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.util.BuiltInMethod;

import java.util.List;

public class KafkaEnumerable extends ConverterImpl implements EnumerableRel {
    KafkaEnumerable(RelOptCluster cluster, RelTraitSet traits, RelNode input) {
        super(cluster, ConventionTraitDef.INSTANCE, traits, input);
    }

    @Override
    public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
        return new KafkaEnumerable(getCluster(), traitSet, sole(inputs));
    }

    @Override
    public EnumerableRel.Result implement(EnumerableRelImplementor relImplementor, EnumerableRel.Prefer prefer) {
        final KafkaRelNode.Implementor implementor = new KafkaRelNode.Implementor();
        implementor.visitChild(0, getInput());

        final RelDataType rowType = getRowType();
        final PhysType physType = PhysTypeImpl.of(relImplementor.getTypeFactory(), rowType,
                prefer.prefer(JavaRowFormat.ARRAY));

        List<Object> resultList = implementor.kafkaTable.poll(implementor);
        BlockStatement block = Blocks.toBlock(Expressions.call(
                BuiltInMethod.AS_ENUMERABLE2.method, Expressions.constant(resultList.toArray())
        ));
        return relImplementor.result(physType, block);
    }
}
