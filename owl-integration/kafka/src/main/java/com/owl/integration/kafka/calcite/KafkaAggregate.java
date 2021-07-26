package com.owl.integration.kafka.calcite;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.InvalidRelException;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.util.ImmutableBitSet;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class KafkaAggregate extends Aggregate implements KafkaRelNode {
    private static final Set<SqlKind> SUPPORTED_AGGREGATIONS = EnumSet.of(
            SqlKind.COUNT,
            SqlKind.MAX,
            SqlKind.MIN);

    protected KafkaAggregate(RelOptCluster cluster,
                             RelTraitSet traitSet,
                             RelNode input,
                             ImmutableBitSet groupSet,
                             List<ImmutableBitSet> groupSets,
                             List<AggregateCall> aggCalls) throws InvalidRelException {
        super(cluster, traitSet, ImmutableList.of(), input, groupSet, groupSets, aggCalls);

        if (getConvention() != input.getConvention()) {
            String message = String.format(Locale.ROOT, "%s != %s", getConvention(),
                    input.getConvention());
            throw new AssertionError(message);
        }

        assert getConvention() == input.getConvention();
        assert getConvention() == KafkaRelNode.CONVENTION;
        assert !this.groupSet.isEmpty() : "Grouping sets not supported";
        assert !this.groupSets.isEmpty() : "Grouping sets not supported";

        for (AggregateCall aggCall : aggCalls) {
            if (aggCall.isDistinct()) {
                final String message = "Distinct aggregations not supported";
                throw new InvalidRelException(message);
            }

            final SqlKind kind = aggCall.getAggregation().getKind();
            if (!SUPPORTED_AGGREGATIONS.contains(kind)) {
                final String message = String.format(Locale.ROOT,
                        "Aggregation %s not supported (use one of %s)", kind, SUPPORTED_AGGREGATIONS);
                throw new InvalidRelException(message);
            }

            if (aggCall.getArgList().size() != 1) {
                final String message = "Multi aggregation args not supported";
                throw new InvalidRelException(message);
            }
        }

        if (getGroupType() != Group.SIMPLE) {
            final String message = String.format(Locale.ROOT, "Only %s grouping is supported. "
                    + "Yours is %s", Group.SIMPLE, getGroupType());
            throw new InvalidRelException(message);
        }
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        final List<String> inputFields = fieldNames(getInput().getRowType());
        for (AggregateCall call : aggCalls) {
            final SqlKind kind = call.getAggregation().getKind();
            String field = inputFields.get(call.getArgList().get(0));
            switch (kind) {
                case COUNT:
                    if (!Const.PARTITION.equals(field)) {
                        throw new UnsupportedOperationException("Only count on PARTITION supported");
                    }
                    implementor.addAgg(KafkaAggCall.CountPartition);
                    break;
                case MAX:
                    if (!Const.OFFSET.equals(field)) {
                        throw new UnsupportedOperationException("Only max on OFFSET supported");
                    }
                    implementor.addAgg(KafkaAggCall.MaxOffset);
                    break;
                case MIN:
                    if (!Const.OFFSET.equals(field)) {
                        throw new UnsupportedOperationException("Only min on OFFSET supported");
                    }
                    implementor.addAgg(KafkaAggCall.MinOffset);
                    break;
                default:
                    final String message = String.format(Locale.ROOT,
                            "Aggregation %s not supported (use one of %s)", kind, SUPPORTED_AGGREGATIONS);
                    throw new UnsupportedOperationException(message);
            }
        }
    }

    @Override
    public Aggregate copy(RelTraitSet traitSet, RelNode input, ImmutableBitSet groupSet, List<ImmutableBitSet> groupSets, List<AggregateCall> aggCalls) {
        try {
            return new KafkaAggregate(getCluster(), traitSet, input,
                    groupSet, groupSets, aggCalls);
        } catch (InvalidRelException e) {
            throw new AssertionError(e);
        }
    }
}
