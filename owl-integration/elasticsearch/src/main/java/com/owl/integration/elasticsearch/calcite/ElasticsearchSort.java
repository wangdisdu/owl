package com.owl.integration.elasticsearch.calcite;

import com.owl.integration.elasticsearch.client.request.sorter.FieldSorter;
import com.owl.integration.elasticsearch.client.request.sorter.Sorter;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rel.RelFieldCollation;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Sort;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;

import java.util.List;

/**
 * Implementation of {@link org.apache.calcite.rel.core.Sort}
 * relational expression in Elasticsearch.
 */
public class ElasticsearchSort extends Sort implements ElasticsearchRelNode {
    public ElasticsearchSort(RelOptCluster cluster, RelTraitSet traitSet, RelNode child,
                      RelCollation collation, RexNode offset, RexNode fetch) {
        super(cluster, traitSet, child, collation, offset, fetch);
        assert getConvention() == ElasticsearchRelNode.CONVENTION;
        assert getConvention() == child.getConvention();
    }

    @Override
    public Sort copy(RelTraitSet traitSet, RelNode relNode, RelCollation relCollation,
                               RexNode offset, RexNode fetch) {
        return new ElasticsearchSort(getCluster(), traitSet, relNode, collation, offset, fetch);
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        final List<RelDataTypeField> fields = getRowType().getFieldList();

        for (RelFieldCollation fieldCollation : collation.getFieldCollations()) {
            //todo udf field
            final String name = fields.get(fieldCollation.getFieldIndex()).getName();
            implementor.addSort(toSorter(name, fieldCollation.getDirection()));
        }

        if (offset != null) {
            implementor.setOffset(((RexLiteral) offset).getValueAs(Integer.class));
        }

        if (fetch != null) {
            implementor.setFetch(((RexLiteral) fetch).getValueAs(Integer.class));
        }
    }

    private static Sorter toSorter(String field, RelFieldCollation.Direction direction) {
        return new FieldSorter().setField(field).setOrder(!direction.isDescending());
    }
}
