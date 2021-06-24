package com.owl.integration.elasticsearch.calcite;

import com.owl.integration.elasticsearch.calcite.analyzer.PredicateAnalyzer;
import com.owl.integration.elasticsearch.client.request.query.QueryBuilders;
import com.owl.integration.elasticsearch.client.request.query.Query;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rex.RexNode;

import java.util.List;

/**
 * Implementation of a {@link org.apache.calcite.rel.core.Filter}
 * relational expression in Elasticsearch.
 */
public class ElasticsearchFilter extends Filter implements ElasticsearchRelNode {

    public ElasticsearchFilter(RelOptCluster cluster,
                               RelTraitSet traitSet,
                               RelNode child,
                               RexNode condition) {
        super(cluster, traitSet, child, condition);
        assert getConvention() == ElasticsearchRelNode.CONVENTION;
        assert getConvention() == child.getConvention();
    }

    @Override
    public Filter copy(RelTraitSet relTraitSet, RelNode input, RexNode condition) {
        return new ElasticsearchFilter(getCluster(), relTraitSet, input, condition);
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq).multiplyBy(0.1);
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        final List<String> inputFields = fieldNames(getInput().getRowType());
        try {
            Query query = QueryBuilders.constantScoreQuery(PredicateAnalyzer.analyze(condition, inputFields));
            implementor.setQuery(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
