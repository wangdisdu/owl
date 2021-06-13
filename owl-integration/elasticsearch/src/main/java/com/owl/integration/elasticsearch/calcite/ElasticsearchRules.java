package com.owl.integration.elasticsearch.calcite;

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
import org.apache.calcite.rel.logical.LogicalProject;

public class ElasticsearchRules {
    public static final RelOptRule[] RULES = {
            ElasticsearchEnumerableRule.INSTANCE,
            ElasticsearchProjectRule.INSTANCE,
            ElasticsearchSortRule.INSTANCE,
            ElasticsearchFilterRule.INSTANCE,
            ElasticsearchAggregateRule.INSTANCE
    };

    /**
     * Base class for planner rules that convert a relational expression to
     * Elasticsearch calling convention.
     */
    abstract static class ElasticsearchConverterRule extends ConverterRule {
        protected ElasticsearchConverterRule(Config config) {
            super(config);
        }
    }

    /**
     * Rule to convert a {@link org.apache.calcite.rel.core.Sort} to an
     * {@link ElasticsearchSort}.
     */
    private static class ElasticsearchSortRule extends ElasticsearchConverterRule {
        private static final ElasticsearchSortRule INSTANCE = Config.INSTANCE
                .withConversion(Sort.class, Convention.NONE,
                        ElasticsearchRelNode.CONVENTION, "ElasticsearchSortRule")
                .withRuleFactory(ElasticsearchSortRule::new)
                .toRule(ElasticsearchSortRule.class);

        protected ElasticsearchSortRule(Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode relNode) {
            final Sort sort = (Sort) relNode;
            final RelTraitSet traitSet = sort.getTraitSet().replace(out).replace(sort.getCollation());
            return new ElasticsearchSort(relNode.getCluster(), traitSet,
                    convert(sort.getInput(), traitSet.replace(RelCollations.EMPTY)), sort.getCollation(),
                    sort.offset, sort.fetch);
        }
    }

    /**
     * Rule to convert a {@link org.apache.calcite.rel.logical.LogicalFilter} to an
     * {@link ElasticsearchFilter}.
     */
    private static class ElasticsearchFilterRule extends ElasticsearchConverterRule {
        private static final ElasticsearchFilterRule INSTANCE = Config.INSTANCE
                .withConversion(LogicalFilter.class, Convention.NONE,
                        ElasticsearchRelNode.CONVENTION, "ElasticsearchFilterRule")
                .withRuleFactory(ElasticsearchFilterRule::new)
                .toRule(ElasticsearchFilterRule.class);

        protected ElasticsearchFilterRule(Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode relNode) {
            final LogicalFilter filter = (LogicalFilter) relNode;
            final RelTraitSet traitSet = filter.getTraitSet().replace(out);
            return new ElasticsearchFilter(relNode.getCluster(), traitSet,
                    convert(filter.getInput(), out),
                    filter.getCondition());
        }
    }

    /**
     * Rule to convert an {@link org.apache.calcite.rel.logical.LogicalAggregate}
     * to an {@link ElasticsearchAggregate}.
     */
    private static class ElasticsearchAggregateRule extends ElasticsearchConverterRule {
        private static final RelOptRule INSTANCE = Config.INSTANCE
                .withConversion(LogicalAggregate.class, Convention.NONE,
                        ElasticsearchRelNode.CONVENTION, "ElasticsearchAggregateRule")
                .withRuleFactory(ElasticsearchAggregateRule::new)
                .toRule(ElasticsearchAggregateRule.class);

        protected ElasticsearchAggregateRule(Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode rel) {
            final LogicalAggregate agg = (LogicalAggregate) rel;
            final RelTraitSet traitSet = agg.getTraitSet().replace(out);
            try {
                return new ElasticsearchAggregate(
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
     * Rule to convert a {@link org.apache.calcite.rel.logical.LogicalProject}
     * to an {@link ElasticsearchProject}.
     */
    private static class ElasticsearchProjectRule extends ElasticsearchConverterRule {
        private static final ElasticsearchProjectRule INSTANCE = Config.INSTANCE
                .withConversion(LogicalProject.class, Convention.NONE,
                        ElasticsearchRelNode.CONVENTION, "ElasticsearchProjectRule")
                .withRuleFactory(ElasticsearchProjectRule::new)
                .toRule(ElasticsearchProjectRule.class);

        protected ElasticsearchProjectRule(Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode relNode) {
            final LogicalProject project = (LogicalProject) relNode;
            final RelTraitSet traitSet = project.getTraitSet().replace(out);
            return new ElasticsearchProject(project.getCluster(), traitSet,
                    convert(project.getInput(), out), project.getProjects(), project.getRowType());
        }
    }

    /**
     * Rule to convert a relational expression from
     * {@link ElasticsearchRelNode#CONVENTION} to {@link EnumerableConvention}.
     */
    private static  class ElasticsearchEnumerableRule extends ConverterRule {
        /** Singleton instance of ElasticsearchEnumerableRule. */
        static final ConverterRule INSTANCE = Config.INSTANCE
                .withConversion(RelNode.class, ElasticsearchRelNode.CONVENTION,
                        EnumerableConvention.INSTANCE,
                        "ElasticsearchEnumerableRule")
                .withRuleFactory(ElasticsearchEnumerableRule::new)
                .toRule(ElasticsearchEnumerableRule.class);

        /** Called from the Config. */
        protected ElasticsearchEnumerableRule(Config config) {
            super(config);
        }

        @Override public RelNode convert(RelNode relNode) {
            RelTraitSet newTraitSet = relNode.getTraitSet().replace(getOutConvention());
            return new ElasticsearchEnumerable(relNode.getCluster(), newTraitSet, relNode);
        }
    }
}
