package com.owl.integration.kafka.calcite;

import org.apache.calcite.adapter.enumerable.EnumerableConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.InvalidRelException;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.Sort;
import org.apache.calcite.rel.logical.LogicalAggregate;
import org.apache.calcite.rel.logical.LogicalFilter;

public class KafkaRules {
    public static final RelOptRule[] RULES = {
            KafkaEnumerableRule.INSTANCE,
            KafkaSortRule.INSTANCE,
            KafkaFilterRule.INSTANCE,
            KafkaAggregateRule.INSTANCE
    };

    /**
     * Base class for planner rules that convert a relational expression to
     * Kafka calling convention.
     */
    abstract static class KafkaConverterRule extends ConverterRule {
        protected KafkaConverterRule(Config config) {
            super(config);
        }
    }

    /**
     * Rule to convert a {@link org.apache.calcite.rel.core.Sort} to an
     * {@link KafkaSort}.
     */
    private static class KafkaSortRule extends KafkaConverterRule {
        private static final KafkaSortRule INSTANCE = ConverterRule.Config.INSTANCE
                .withConversion(Sort.class, Convention.NONE,
                        KafkaRelNode.CONVENTION, "KafkaSortRule")
                .withRuleFactory(KafkaSortRule::new)
                .toRule(KafkaSortRule.class);

        protected KafkaSortRule(ConverterRule.Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode relNode) {
            final Sort sort = (Sort) relNode;
            final RelTraitSet traitSet = sort.getTraitSet().replace(out).replace(sort.getCollation());
            return new KafkaSort(relNode.getCluster(), traitSet,
                    convert(sort.getInput(), traitSet.replace(RelCollations.EMPTY)), sort.getCollation(),
                    sort.offset, sort.fetch);
        }
    }

    /**
     * Rule to convert a {@link org.apache.calcite.rel.logical.LogicalFilter} to an
     * {@link KafkaFilter}.
     */
    private static class KafkaFilterRule extends KafkaConverterRule {
        private static final KafkaFilterRule INSTANCE = ConverterRule.Config.INSTANCE
                .withConversion(LogicalFilter.class, Convention.NONE,
                        KafkaRelNode.CONVENTION, "KafkaFilterRule")
                .withRuleFactory(KafkaFilterRule::new)
                .toRule(KafkaFilterRule.class);

        protected KafkaFilterRule(ConverterRule.Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode relNode) {
            final LogicalFilter filter = (LogicalFilter) relNode;
            final RelTraitSet traitSet = filter.getTraitSet().replace(out);
            return new KafkaFilter(relNode.getCluster(), traitSet,
                    convert(filter.getInput(), out),
                    filter.getCondition());
        }
    }

    /**
     * Rule to convert an {@link org.apache.calcite.rel.logical.LogicalAggregate}
     * to an {@link KafkaAggregate}.
     */
    private static class KafkaAggregateRule extends KafkaConverterRule {
        private static final RelOptRule INSTANCE = ConverterRule.Config.INSTANCE
                .withConversion(LogicalAggregate.class, Convention.NONE,
                        KafkaRelNode.CONVENTION, "KafkaAggregateRule")
                .withRuleFactory(KafkaAggregateRule::new)
                .toRule(KafkaAggregateRule.class);

        protected KafkaAggregateRule(ConverterRule.Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode rel) {
            final LogicalAggregate agg = (LogicalAggregate) rel;
            final RelTraitSet traitSet = agg.getTraitSet().replace(out);
            try {
                return new KafkaAggregate(
                        rel.getCluster(),
                        traitSet,
                        convert(agg.getInput(), traitSet.simplify()),
                        agg.getGroupSet(),
                        agg.getGroupSets(),
                        agg.getAggCallList());
            } catch (InvalidRelException e) {
                return null;
            }
        }
    }

    /**
     * Rule to convert a relational expression from
     * {@link KafkaRelNode#CONVENTION} to {@link EnumerableConvention}.
     */
    private static  class KafkaEnumerableRule extends ConverterRule {
        /** Singleton instance of KafkaEnumerableRule. */
        static final ConverterRule INSTANCE = Config.INSTANCE
                .withConversion(RelNode.class, KafkaRelNode.CONVENTION,
                        EnumerableConvention.INSTANCE,
                        "KafkaEnumerableRule")
                .withRuleFactory(KafkaEnumerableRule::new)
                .toRule(KafkaEnumerableRule.class);

        /** Called from the Config. */
        protected KafkaEnumerableRule(Config config) {
            super(config);
        }

        @Override public RelNode convert(RelNode relNode) {
            RelTraitSet newTraitSet = relNode.getTraitSet().replace(getOutConvention());
            return new KafkaEnumerable(relNode.getCluster(), newTraitSet, relNode);
        }
    }
}
