package com.owl.integration.elasticsearch.calcite;

import com.google.common.collect.ImmutableList;
import com.owl.integration.elasticsearch.Constants;
import com.owl.integration.elasticsearch.client.request.aggregation.*;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.InvalidRelException;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.util.ImmutableBitSet;

import java.util.*;

/**
 * Implementation of
 * {@link org.apache.calcite.rel.core.Aggregate} relational expression
 * for ElasticSearch.
 */
public class ElasticsearchAggregate extends Aggregate implements ElasticsearchRelNode {

    private static final Set<SqlKind> SUPPORTED_AGGREGATIONS = EnumSet.of(
            SqlKind.COUNT,
            SqlKind.MAX,
            SqlKind.MIN,
            SqlKind.AVG,
            SqlKind.SUM,
            SqlKind.ANY_VALUE);

    /** Creates an ElasticsearchAggregate. */
    public ElasticsearchAggregate(RelOptCluster cluster,
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
        assert getConvention() == ElasticsearchRelNode.CONVENTION;
        assert this.groupSets.size() == 1 : "Grouping sets not supported";

        for (AggregateCall aggCall : aggCalls) {
            if (aggCall.isDistinct() && !aggCall.isApproximate()) {
                final String message = String.format(Locale.ROOT, "Only approximate distinct "
                                + "aggregations are supported in Elastic (cardinality aggregation). Use %s function",
                        SqlStdOperatorTable.APPROX_COUNT_DISTINCT.getName());
                throw new InvalidRelException(message);
            }

            final SqlKind kind = aggCall.getAggregation().getKind();
            if (!SUPPORTED_AGGREGATIONS.contains(kind)) {
                final String message = String.format(Locale.ROOT,
                        "Aggregation %s not supported (use one of %s)", kind, SUPPORTED_AGGREGATIONS);
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
    public Aggregate copy(RelTraitSet traitSet,
                          RelNode input,
                          ImmutableBitSet groupSet,
                          List<ImmutableBitSet> groupSets,
                          List<AggregateCall> aggCalls) {
        try {
            return new ElasticsearchAggregate(getCluster(), traitSet, input,
                    groupSet, groupSets, aggCalls);
        } catch (InvalidRelException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        final List<String> inputFields = fieldNames(getInput().getRowType());
        Map<String, Aggregation> aggregations = new LinkedHashMap<>();
        BucketAggregation lastBucket = null;
        for (int group : groupSet) {
            //todo udf field
            final String name = inputFields.get(group);
            BucketAggregation bucket = toBucketAggregation(name);
            if(lastBucket == null) {
                aggregations.put(name, bucket);
            } else {
                lastBucket.addSubAggregation(name, bucket);
            }
            lastBucket = bucket;
        }

        for (AggregateCall aggCall : aggCalls) {
            final List<String> names = new ArrayList<>();
            for (int i : aggCall.getArgList()) {
                names.add(inputFields.get(i));
            }
            //todo udf field
            final String name = names.isEmpty() ? Constants.ID : names.get(0);
            Aggregation metric = toMetricAggregation(name, aggCall);
            if(lastBucket == null) {
                aggregations.put(name, metric);
            } else {
                lastBucket.addSubAggregation(name, metric);
            }
        }
        implementor.setAggregation(aggregations);
    }

    private static BucketAggregation toBucketAggregation(String field) {
        return new TermsAggregation().setField(field);
    }

    private static Aggregation toMetricAggregation(String field, AggregateCall call) {
        final SqlKind kind = call.getAggregation().getKind();
        switch (kind) {
            case COUNT:
                // approx_count_distinct() vs count()
                if (call.isDistinct() && call.isApproximate()) {
                    return new CardinalityAggregation().setField(field);
                } else {
                    return new ValueCountAggregation().setField(field);
                }
            case SUM:
                return new SumAggregation().setField(field);
            case MIN:
                return new MinAggregation().setField(field);
            case MAX:
                return new MaxAggregation().setField(field);
            case AVG:
                return new AvgAggregation().setField(field);
            case ANY_VALUE:
                return new TermsAggregation().setField(field).setSize(1);
            default:
                throw new IllegalArgumentException("Unknown aggregation kind " + kind + " for " + call);
        }
    }
}
