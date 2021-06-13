package com.owl.integration.elasticsearch.calcite;

import com.google.common.collect.ImmutableList;
import com.owl.integration.elasticsearch.calcite.analyzer.ExpressionVisitor;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Project;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.util.Pair;

import java.util.List;

/**
 * Implementation of {@link org.apache.calcite.rel.core.Project}
 * relational expression in Elasticsearch.
 */
public class ElasticsearchProject extends Project implements ElasticsearchRelNode {

    public ElasticsearchProject(RelOptCluster cluster, RelTraitSet traitSet, RelNode input,
                         List<? extends RexNode> projects, RelDataType rowType) {
        super(cluster, traitSet, ImmutableList.of(), input, projects, rowType);
        assert getConvention() == ElasticsearchRelNode.CONVENTION;
        assert getConvention() == input.getConvention();
    }

    @Override
    public Project copy(RelTraitSet relTraitSet, RelNode input, List<RexNode> projects,
                        RelDataType relDataType) {
        return new ElasticsearchProject(getCluster(), traitSet, input, projects, relDataType);
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        final List<String> inputFields = fieldNames(getInput().getRowType());
        final ExpressionVisitor visitor = new ExpressionVisitor(inputFields);
        final List<Pair<RexNode, String>> pairs = getNamedProjects();
        for (Pair<RexNode, String> pair: pairs) {
            final String alias = pair.right;
            final String field = pair.left.accept(visitor);
            implementor.addField(field, alias);
        }
    }

}
