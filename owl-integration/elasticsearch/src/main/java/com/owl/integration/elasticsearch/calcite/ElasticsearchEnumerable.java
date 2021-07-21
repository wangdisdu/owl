package com.owl.integration.elasticsearch.calcite;

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

/**
 * Relational expression representing a scan of a table in an Elasticsearch data source.
 */
public class ElasticsearchEnumerable extends ConverterImpl implements EnumerableRel {
    ElasticsearchEnumerable(RelOptCluster cluster, RelTraitSet traits, RelNode input) {
        super(cluster, ConventionTraitDef.INSTANCE, traits, input);
    }

    @Override
    public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
        return new ElasticsearchEnumerable(getCluster(), traitSet, sole(inputs));
    }

    @Override
    public EnumerableRel.Result implement(EnumerableRelImplementor relImplementor, EnumerableRel.Prefer prefer) {
        final ElasticsearchRelNode.Implementor implementor = new ElasticsearchRelNode.Implementor();
        implementor.visitChild(0, getInput());

        final RelDataType rowType = getRowType();
        final PhysType physType = PhysTypeImpl.of(relImplementor.getTypeFactory(), rowType,
                prefer.prefer(JavaRowFormat.ARRAY));

        ElasticsearchTable esTable = implementor.elasticsearchTable;
        List<Object[]> resultList = esTable.search(implementor);
        BlockStatement block = Blocks.toBlock(Expressions.call(
                BuiltInMethod.AS_ENUMERABLE2.method, Expressions.constant(resultList.toArray())
        ));
        return relImplementor.result(physType, block);
    }
}
