package com.owl.integration.kafka.calcite;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.util.Pair;
import org.apache.calcite.util.Sarg;

import java.util.List;
import java.util.Set;

public class KafkaFilter extends Filter implements KafkaRelNode {


    protected KafkaFilter(RelOptCluster cluster,
                          RelTraitSet traits,
                          RelNode child, RexNode
                                  condition) {
        super(cluster, traits, child, condition);
        assert getConvention() == KafkaRelNode.CONVENTION;
        assert getConvention() == child.getConvention();
    }

    @Override
    public Filter copy(RelTraitSet relTraitSet, RelNode input, RexNode condition) {
        return new KafkaFilter(getCluster(), relTraitSet, input, condition);
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq).multiplyBy(0.1);
    }

    @Override
    public void implement(Implementor implementor) {
        implementor.visitChild(0, getInput());
        final List<String> inputFields = fieldNames(getInput().getRowType());
        final List<RexNode> andNodes = RelOptUtil.conjunctions(condition);
        for (RexNode andNode : andNodes) {
            SqlKind kind = andNode.getKind();
            switch (kind) {
                case SEARCH:
                    translateSearch(implementor, inputFields, (RexCall) andNode);
                    break;
                case EQUALS:
                    translateEquals(implementor, inputFields, (RexCall) andNode);
                    break;
                case LESS_THAN:
                case LESS_THAN_OR_EQUAL:
                    translateLessThan(implementor, inputFields, (RexCall) andNode);
                    break;
                case GREATER_THAN:
                case GREATER_THAN_OR_EQUAL:
                    translateGreaterThan(implementor, inputFields, (RexCall) andNode);
                    break;
                default:
                    throw new UnsupportedOperationException("cannot translate " + andNode);
            }
        }
    }

    private void translateSearch(Implementor implementor, List<String> fields, RexCall call) {
        Pair<String, RexLiteral> pair = translateBinary(fields, call);
        String field = pair.left;
        RexLiteral literal = pair.right;
        Sarg sarg = literal.getValueAs(Sarg.class);
        if (sarg == null) {
            throw new UnsupportedOperationException("cannot translate call " + call);
        }
        Set<Range> ranges = sarg.rangeSet.asRanges();
        if (sarg.isPoints()) {
            if (Const.PARTITION.equals(field)) {
                ranges.forEach(i -> implementor.addPartition(
                        ((Number) i.lowerEndpoint()).intValue()
                ));
                return;
            }
        } else {
            if (Const.OFFSET.equals(field)) {
                ranges.forEach(i -> {
                    if (i.hasLowerBound()) {
                        long lower = ((Number) i.lowerEndpoint()).longValue();
                        if (BoundType.OPEN == i.lowerBoundType()) {
                            lower = lower + 1;
                        }
                        implementor.setOffsetFrom(lower);
                    }
                    if (i.hasUpperBound()) {
                        long upper = ((Number) i.upperEndpoint()).longValue();
                        if (BoundType.OPEN == i.lowerBoundType()) {
                            upper = upper - 1;
                        }
                        implementor.setOffsetTo(upper);
                    }
                });
                return;
            }
        }
        throw new UnsupportedOperationException("cannot translate call " + call);
    }

    private void translateEquals(Implementor implementor, List<String> fields, RexCall call) {
        Pair<String, RexLiteral> pair = translateBinary(fields, call);
        if (Const.PARTITION.equals(pair.left)) {
            Integer partition = pair.right.getValueAs(Integer.class);
            implementor.addPartition(partition);
        } else if (Const.OFFSET.equals(pair.left)) {
            Long offset = pair.right.getValueAs(Long.class);
            implementor.setOffsetFrom(offset);
            implementor.setOffsetTo(offset);
        } else {
            throw new UnsupportedOperationException("cannot translate call " + call);
        }
    }

    private void translateLessThan(Implementor implementor, List<String> fields, RexCall call) {
        SqlKind kind = call.getKind();
        Pair<String, RexLiteral> pair = translateBinary(fields, call);
        if (Const.OFFSET.equals(pair.left)) {
            Long offset =  pair.right.getValueAs(Long.class);
            if (SqlKind.LESS_THAN == kind) {
                offset = offset - 1;
            }
            implementor.setOffsetTo(offset);
        } else {
            throw new UnsupportedOperationException("cannot translate call " + call);
        }
    }

    private void translateGreaterThan(Implementor implementor, List<String> fields, RexCall call) {
        SqlKind kind = call.getKind();
        Pair<String, RexLiteral> pair = translateBinary(fields, call);
        if (Const.OFFSET.equals(pair.left)) {
            Long offset = pair.right.getValueAs(Long.class);
            if (SqlKind.GREATER_THAN == kind) {
                offset = offset + 1;
            }
            implementor.setOffsetFrom(offset);
        } else {
            throw new UnsupportedOperationException("cannot translate call " + call);
        }
    }

    private Pair<String, RexLiteral> translateBinary(List<String> fields, RexCall call) {
        if (call.operands.size() != 2) {
            throw new UnsupportedOperationException("cannot translate call " + call);
        }
        final RexNode left = call.operands.get(0);
        final RexNode right = call.operands.get(1);
        if (left.getKind() == SqlKind.INPUT_REF && right.getKind() == SqlKind.LITERAL) {
            String field = fields.get(((RexInputRef) left).getIndex());
            return Pair.of(field, (RexLiteral) right);
        } else if (right.getKind() == SqlKind.INPUT_REF && left.getKind() == SqlKind.LITERAL) {
            String field = fields.get(((RexInputRef) right).getIndex());
            return Pair.of(field, (RexLiteral) left);
        }
        throw new UnsupportedOperationException("cannot translate call " + call);
    }
}
