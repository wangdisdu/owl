/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.owl.integration.mongodb.calcite;

import org.apache.calcite.adapter.enumerable.RexImpTable;
import org.apache.calcite.adapter.enumerable.RexToLixTranslator;
import org.apache.calcite.adapter.java.JavaTypeFactory;
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
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexVisitorImpl;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.validate.SqlValidatorUtil;
import org.apache.calcite.util.Bug;
import org.apache.calcite.util.Util;
import org.apache.calcite.util.trace.CalciteTrace;
import org.slf4j.Logger;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rules and relational operators for
 * {@link MongoRel#CONVENTION MONGO}
 * calling convention.
 */
public class MongoRules {
    private MongoRules() {
    }

    protected static final Logger LOGGER = CalciteTrace.getPlannerTracer();

    public static final RelOptRule[] RULES = {
            MongoSortRule.INSTANCE,
            MongoFilterRule.INSTANCE,
            MongoProjectRule.INSTANCE,
            MongoAggregateRule.INSTANCE,
    };

    /**
     * Returns 'string' if it is a call to item['string'], null otherwise.
     */
    static String isItem(RexCall call) {
        if (call.getOperator() != SqlStdOperatorTable.ITEM) {
            return null;
        }
        final RexNode op0 = call.operands.get(0);
        final RexNode op1 = call.operands.get(1);
        if (op0 instanceof RexInputRef
                && ((RexInputRef) op0).getIndex() == 0
                && op1 instanceof RexLiteral
                && ((RexLiteral) op1).getValue2() instanceof String) {
            return (String) ((RexLiteral) op1).getValue2();
        }
        return null;
    }

    static List<String> mongoFieldNames(final RelDataType rowType) {
        return SqlValidatorUtil.uniquify(
                new AbstractList<String>() {
                    @Override
                    public String get(int index) {
                        final String name = rowType.getFieldList().get(index).getName();
                        return name.startsWith("$") ? "_" + name.substring(2) : name;
                    }

                    @Override
                    public int size() {
                        return rowType.getFieldCount();
                    }
                },
                SqlValidatorUtil.EXPR_SUGGESTER, true);
    }

    static String maybeQuote(String s) {
        if (!needsQuote(s)) {
            return s;
        }
        return quote(s);
    }

    static String quote(String s) {
        return "'" + s + "'"; // TODO: handle embedded quotes
    }

    private static boolean needsQuote(String s) {
        for (int i = 0, n = s.length(); i < n; i++) {
            char c = s.charAt(i);
            if (!Character.isJavaIdentifierPart(c)
                    || c == '$') {
                return true;
            }
        }
        return false;
    }

    /**
     * Translator from {@link RexNode} to strings in MongoDB's expression
     * language.
     */
    static class RexToMongoTranslator extends RexVisitorImpl<String> {
        private final JavaTypeFactory typeFactory;
        private final List<String> inFields;

        private static final Map<SqlOperator, String> MONGO_OPERATORS =
                new HashMap<>();

        static {
            // Arithmetic
            MONGO_OPERATORS.put(SqlStdOperatorTable.DIVIDE, "$divide");
            MONGO_OPERATORS.put(SqlStdOperatorTable.MULTIPLY, "$multiply");
            MONGO_OPERATORS.put(SqlStdOperatorTable.MOD, "$mod");
            MONGO_OPERATORS.put(SqlStdOperatorTable.PLUS, "$add");
            MONGO_OPERATORS.put(SqlStdOperatorTable.MINUS, "$subtract");
            // Boolean
            MONGO_OPERATORS.put(SqlStdOperatorTable.AND, "$and");
            MONGO_OPERATORS.put(SqlStdOperatorTable.OR, "$or");
            MONGO_OPERATORS.put(SqlStdOperatorTable.NOT, "$not");
            // Comparison
            MONGO_OPERATORS.put(SqlStdOperatorTable.EQUALS, "$eq");
            MONGO_OPERATORS.put(SqlStdOperatorTable.NOT_EQUALS, "$ne");
            MONGO_OPERATORS.put(SqlStdOperatorTable.GREATER_THAN, "$gt");
            MONGO_OPERATORS.put(SqlStdOperatorTable.GREATER_THAN_OR_EQUAL, "$gte");
            MONGO_OPERATORS.put(SqlStdOperatorTable.LESS_THAN, "$lt");
            MONGO_OPERATORS.put(SqlStdOperatorTable.LESS_THAN_OR_EQUAL, "$lte");
        }

        protected RexToMongoTranslator(JavaTypeFactory typeFactory,
                                       List<String> inFields) {
            super(true);
            this.typeFactory = typeFactory;
            this.inFields = inFields;
        }

        @Override
        public String visitLiteral(RexLiteral literal) {
            if (literal.getValue() == null) {
                return "null";
            }
            return "{$literal: "
                    + RexToLixTranslator.translateLiteral(literal, literal.getType(),
                    typeFactory, RexImpTable.NullAs.NOT_POSSIBLE)
                    + "}";
        }

        @Override
        public String visitInputRef(RexInputRef inputRef) {
            return maybeQuote(
                    "$" + inFields.get(inputRef.getIndex()));
        }

        @Override
        public String visitCall(RexCall call) {
            String name = isItem(call);
            if (name != null) {
                return "'$" + name + "'";
            }
            final List<String> strings = visitList(call.operands);
            if (call.getKind() == SqlKind.CAST) {
                return strings.get(0);
            }
            String stdOperator = MONGO_OPERATORS.get(call.getOperator());
            if (stdOperator != null) {
                return "{" + stdOperator + ": [" + Util.commaList(strings) + "]}";
            }
            if (call.getOperator() == SqlStdOperatorTable.ITEM) {
                final RexNode op1 = call.operands.get(1);
                if (op1 instanceof RexLiteral
                        && op1.getType().getSqlTypeName() == SqlTypeName.INTEGER) {
                    if (!Bug.CALCITE_194_FIXED) {
                        return "'" + stripQuotes(strings.get(0)) + "["
                                + ((RexLiteral) op1).getValue2() + "]'";
                    }
                    return strings.get(0) + "[" + strings.get(1) + "]";
                }
            }
            if (call.getOperator() == SqlStdOperatorTable.CASE) {
                StringBuilder sb = new StringBuilder();
                StringBuilder finish = new StringBuilder();
                // case(a, b, c)  -> $cond:[a, b, c]
                // case(a, b, c, d) -> $cond:[a, b, $cond:[c, d, null]]
                // case(a, b, c, d, e) -> $cond:[a, b, $cond:[c, d, e]]
                for (int i = 0; i < strings.size(); i += 2) {
                    sb.append("{$cond:[");
                    finish.append("]}");

                    sb.append(strings.get(i));
                    sb.append(',');
                    sb.append(strings.get(i + 1));
                    sb.append(',');
                    if (i == strings.size() - 3) {
                        sb.append(strings.get(i + 2));
                        break;
                    }
                    if (i == strings.size() - 2) {
                        sb.append("null");
                        break;
                    }
                }
                sb.append(finish);
                return sb.toString();
            }
            throw new IllegalArgumentException("Translation of " + call.toString()
                    + " is not supported by MongoProject");
        }

        private String stripQuotes(String s) {
            return s.startsWith("'") && s.endsWith("'")
                    ? s.substring(1, s.length() - 1)
                    : s;
        }
    }

    /**
     * Base class for planner rules that convert a relational expression to
     * MongoDB calling convention.
     */
    abstract static class MongoConverterRule extends ConverterRule {
        protected MongoConverterRule(Config config) {
            super(config);
        }
    }

    /**
     * Rule to convert a {@link org.apache.calcite.rel.core.Sort} to a
     * {@link MongoSort}.
     */
    private static class MongoSortRule extends MongoConverterRule {
        static final MongoSortRule INSTANCE = Config.INSTANCE
                .withConversion(Sort.class, Convention.NONE, MongoRel.CONVENTION,
                        "MongoSortRule")
                .withRuleFactory(MongoSortRule::new)
                .toRule(MongoSortRule.class);

        MongoSortRule(Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode rel) {
            final Sort sort = (Sort) rel;
            final RelTraitSet traitSet =
                    sort.getTraitSet().replace(out)
                            .replace(sort.getCollation());
            return new MongoSort(rel.getCluster(), traitSet,
                    convert(sort.getInput(), traitSet.replace(RelCollations.EMPTY)),
                    sort.getCollation(), sort.offset, sort.fetch);
        }
    }

    /**
     * Rule to convert a {@link org.apache.calcite.rel.logical.LogicalFilter} to a
     * {@link MongoFilter}.
     */
    private static class MongoFilterRule extends MongoConverterRule {
        static final MongoFilterRule INSTANCE = Config.INSTANCE
                .withConversion(LogicalFilter.class, Convention.NONE,
                        MongoRel.CONVENTION, "MongoFilterRule")
                .withRuleFactory(MongoFilterRule::new)
                .toRule(MongoFilterRule.class);

        MongoFilterRule(Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode rel) {
            final LogicalFilter filter = (LogicalFilter) rel;
            final RelTraitSet traitSet = filter.getTraitSet().replace(out);
            return new MongoFilter(
                    rel.getCluster(),
                    traitSet,
                    convert(filter.getInput(), out),
                    filter.getCondition());
        }
    }

    /**
     * Rule to convert a {@link org.apache.calcite.rel.logical.LogicalProject}
     * to a {@link MongoProject}.
     */
    private static class MongoProjectRule extends MongoConverterRule {
        static final MongoProjectRule INSTANCE = Config.INSTANCE
                .withConversion(LogicalProject.class, Convention.NONE,
                        MongoRel.CONVENTION, "MongoProjectRule")
                .withRuleFactory(MongoProjectRule::new)
                .toRule(MongoProjectRule.class);

        MongoProjectRule(Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode rel) {
            final LogicalProject project = (LogicalProject) rel;
            final RelTraitSet traitSet = project.getTraitSet().replace(out);
            return new MongoProject(project.getCluster(), traitSet,
                    convert(project.getInput(), out), project.getProjects(),
                    project.getRowType());
        }
    }

    /**
     * Rule to convert an {@link org.apache.calcite.rel.logical.LogicalAggregate}
     * to an {@link MongoAggregate}.
     */
    private static class MongoAggregateRule extends MongoConverterRule {
        static final MongoAggregateRule INSTANCE = Config.INSTANCE
                .withConversion(LogicalAggregate.class, Convention.NONE,
                        MongoRel.CONVENTION, "MongoAggregateRule")
                .withRuleFactory(MongoAggregateRule::new)
                .toRule(MongoAggregateRule.class);

        MongoAggregateRule(Config config) {
            super(config);
        }

        @Override
        public RelNode convert(RelNode rel) {
            final LogicalAggregate agg = (LogicalAggregate) rel;
            final RelTraitSet traitSet =
                    agg.getTraitSet().replace(out);
            try {
                return new MongoAggregate(
                        rel.getCluster(),
                        traitSet,
                        convert(agg.getInput(), traitSet.simplify()),
                        agg.getGroupSet(),
                        agg.getGroupSets(),
                        agg.getAggCallList());
            } catch (InvalidRelException e) {
                LOGGER.warn(e.toString());
                return null;
            }
        }
    }

}
