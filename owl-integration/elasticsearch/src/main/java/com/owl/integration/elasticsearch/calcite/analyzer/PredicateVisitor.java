package com.owl.integration.elasticsearch.calcite.analyzer;

import com.google.common.base.Preconditions;
import com.owl.integration.elasticsearch.Constants;
import com.owl.integration.elasticsearch.client.request.query.Query;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexVisitorImpl;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlSyntax;
import org.apache.calcite.sql.type.SqlTypeFamily;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.String.format;

/**
 * Traverses {@link RexNode} tree and builds ES {@link Query}.
 */
public class PredicateVisitor extends RexVisitorImpl<Expression> {
    private final List<String> fields;

    public PredicateVisitor(List<String> fields) {
        super(true);
        this.fields = fields;
    }

    @Override
    public Expression visitInputRef(RexInputRef inputRef) {
        return new NamedFieldExpression(fields.get(inputRef.getIndex()));
    }

    @Override
    public Expression visitLiteral(RexLiteral literal) {
        return new LiteralExpression(literal);
    }

    @Override
    public Expression visitCall(RexCall call) {
        SqlSyntax syntax = call.getOperator().getSyntax();
        if (!supportedRexCall(call)) {
            String message = String.format(Locale.ROOT, "Unsupported call: [%s]", call);
            throw new PredicateAnalyzerException(message);
        }

        switch (syntax) {
            case BINARY:
                return binary(call);
            case POSTFIX:
                return postfix(call);
            case PREFIX:
                return prefix(call);
            case SPECIAL:
                switch (call.getKind()) {
                    case CAST:
                        return toCastExpression(call);
                    case LIKE:
                        return binary(call);
                    case CONTAINS:
                        return binary(call);
                    default:
                        // manually process ITEM($0, 'foo') which in our case will be named attribute
                        if (call.getOperator().getName().equalsIgnoreCase("ITEM")) {
                            return toNamedField((RexLiteral) call.getOperands().get(1));
                        }
                        throw analyzerException(call, syntax);
                }
            case FUNCTION:
                if (call.getOperator().getName().equalsIgnoreCase("CONTAINS")) {
                    List<Expression> operands = visitList(call.getOperands());
                    String query = convertQueryString(operands.subList(0, operands.size() - 1),
                            operands.get(operands.size() - 1));
                    return QueryExpression.create(new NamedFieldExpression("")).queryString(query);
                }
                throw analyzerException(call, syntax);
            case INTERNAL:
                if (call.getKind() == SqlKind.SEARCH) {
                    return binary(call);
                }
                throw analyzerException(call, syntax);
            default:
                throw analyzerException(call, syntax);
        }
    }

    private PredicateAnalyzerException analyzerException(RexCall call, SqlSyntax syntax) {
        String message = format(Locale.ROOT, "Unsupported syntax [%s] for call: [%s]",
                syntax, call);
        return new PredicateAnalyzerException(message);
    }

    private boolean supportedRexCall(RexCall call) {
        final SqlSyntax syntax = call.getOperator().getSyntax();
        switch (syntax) {
            case BINARY:
                switch (call.getKind()) {
                    case CONTAINS:
                    case AND:
                    case OR:
                    case LIKE:
                    case EQUALS:
                    case NOT_EQUALS:
                    case GREATER_THAN:
                    case GREATER_THAN_OR_EQUAL:
                    case LESS_THAN:
                    case LESS_THAN_OR_EQUAL:
                        return true;
                    default:
                        return false;
                }
            case SPECIAL:
                switch (call.getKind()) {
                    case CAST:
                    case LIKE:
                    case ITEM:
                    case OTHER_FUNCTION:
                        return true;
                    case CASE:
                    case SIMILAR:
                    default:
                        return false;
                }
            case FUNCTION:
                return true;
            case POSTFIX:
                switch (call.getKind()) {
                    case IS_NOT_NULL:
                    case IS_NULL:
                        return true;
                    default:
                        return false;
                }
            case PREFIX: // NOT()
                switch (call.getKind()) {
                    case NOT:
                        return true;
                    default:
                        return false;
                }
            case INTERNAL:
                switch (call.getKind()) {
                    case SEARCH:
                        return true;
                    default:
                        return false;
                }
                // fall through
            case FUNCTION_ID:
            case FUNCTION_STAR:
            default:
                return false;
        }
    }

    private static String convertQueryString(List<Expression> fields, Expression query) {
        int index = 0;
        Preconditions.checkArgument(query instanceof LiteralExpression,
                "Query string must be a string literal");
        String queryString = ((LiteralExpression) query).stringValue();
        Map<String, String> fieldMap = new LinkedHashMap<>();
        for (Expression expr : fields) {
            if (expr instanceof NamedFieldExpression) {
                NamedFieldExpression field = (NamedFieldExpression) expr;
                String fieldIndexString = String.format(Locale.ROOT, "$%d", index++);
                fieldMap.put(fieldIndexString, field.getReference());
            }
        }
        try {
            return queryString;
        } catch (Exception e) {
            throw new PredicateAnalyzerException(e);
        }
    }

    private QueryExpression prefix(RexCall call) {
        Preconditions.checkArgument(call.getKind() == SqlKind.NOT,
                "Expected %s got %s", SqlKind.NOT, call.getKind());

        if (call.getOperands().size() != 1) {
            String message = String.format(Locale.ROOT, "Unsupported NOT operator: [%s]", call);
            throw new PredicateAnalyzerException(message);
        }

        QueryExpression expr = (QueryExpression) call.getOperands().get(0).accept(this);
        return expr.not();
    }

    private QueryExpression postfix(RexCall call) {
        Preconditions.checkArgument(call.getKind() == SqlKind.IS_NULL
                || call.getKind() == SqlKind.IS_NOT_NULL);
        if (call.getOperands().size() != 1) {
            String message = String.format(Locale.ROOT, "Unsupported operator: [%s]", call);
            throw new PredicateAnalyzerException(message);
        }
        Expression a = call.getOperands().get(0).accept(this);
        // Elasticsearch does not want is null/is not null (exists query)
        // for _id and _index, although it supports for all other metadata column
        isColumn(a, call, Constants.ID, true);
        isColumn(a, call, Constants.INDEX, true);
        QueryExpression operand = QueryExpression.create((TerminalExpression) a);
        return call.getKind() == SqlKind.IS_NOT_NULL ? operand.exists() : operand.notExists();
    }

    /**
     * Process a call which is a binary operation, transforming into an equivalent
     * query expression. Note that the incoming call may be either a simple binary
     * expression, such as {@code foo > 5}, or it may be several simple expressions connected
     * by {@code AND} or {@code OR} operators, such as {@code foo > 5 AND bar = 'abc' AND 'rot' < 1}
     *
     * @param call existing call
     * @return evaluated expression
     */
    private QueryExpression binary(RexCall call) {

        // if AND/OR, do special handling
        if (call.getKind() == SqlKind.AND || call.getKind() == SqlKind.OR) {
            return andOr(call);
        }

        checkForIncompatibleDateTimeOperands(call);

        Preconditions.checkState(call.getOperands().size() == 2);
        final Expression a = call.getOperands().get(0).accept(this);
        final Expression b = call.getOperands().get(1).accept(this);

        final SwapResult pair = swap(a, b);
        final boolean swapped = pair.isSwapped();

        // For _id and _index columns, only equals/not_equals work!
        if (isColumn(pair.getKey(), call, Constants.ID, false)
                || isColumn(pair.getKey(), call, Constants.INDEX, false)
                || isColumn(pair.getKey(), call, Constants.UID, false)) {
            switch (call.getKind()) {
                case EQUALS:
                case NOT_EQUALS:
                    break;
                default:
                    throw new PredicateAnalyzerException(
                            "Cannot handle " + call.getKind() + " expression for _id field, " + call);
            }
        }

        switch (call.getKind()) {
            case SEARCH:
                return QueryExpression.create(pair.getKey()).equals(pair.getValue());
            case CONTAINS:
                return QueryExpression.create(pair.getKey()).contains(pair.getValue());
            case LIKE:
                throw new UnsupportedOperationException("LIKE not yet supported");
            case EQUALS:
                return QueryExpression.create(pair.getKey()).equals(pair.getValue());
            case NOT_EQUALS:
                return QueryExpression.create(pair.getKey()).notEquals(pair.getValue());
            case GREATER_THAN:
                if (swapped) {
                    return QueryExpression.create(pair.getKey()).lt(pair.getValue());
                }
                return QueryExpression.create(pair.getKey()).gt(pair.getValue());
            case GREATER_THAN_OR_EQUAL:
                if (swapped) {
                    return QueryExpression.create(pair.getKey()).lte(pair.getValue());
                }
                return QueryExpression.create(pair.getKey()).gte(pair.getValue());
            case LESS_THAN:
                if (swapped) {
                    return QueryExpression.create(pair.getKey()).gt(pair.getValue());
                }
                return QueryExpression.create(pair.getKey()).lt(pair.getValue());
            case LESS_THAN_OR_EQUAL:
                if (swapped) {
                    return QueryExpression.create(pair.getKey()).gte(pair.getValue());
                }
                return QueryExpression.create(pair.getKey()).lte(pair.getValue());
            default:
                break;
        }
        String message = String.format(Locale.ROOT, "Unable to handle call: [%s]", call);
        throw new PredicateAnalyzerException(message);
    }

    private QueryExpression andOr(RexCall call) {
        QueryExpression[] expressions = new QueryExpression[call.getOperands().size()];
        PredicateAnalyzerException firstError = null;
        boolean partial = false;
        for (int i = 0; i < call.getOperands().size(); i++) {
            try {
                Expression expr = call.getOperands().get(i).accept(this);
                if (expr instanceof NamedFieldExpression) {
                    // nop currently
                } else {
                    expressions[i] = (QueryExpression) call.getOperands().get(i).accept(this);
                }
                partial |= expressions[i].isPartial();
            } catch (PredicateAnalyzerException e) {
                if (firstError == null) {
                    firstError = e;
                }
                partial = true;
            }
        }

        switch (call.getKind()) {
            case OR:
                if (partial) {
                    if (firstError != null) {
                        throw firstError;
                    } else {
                        final String message = String.format(Locale.ROOT, "Unable to handle call: [%s]", call);
                        throw new PredicateAnalyzerException(message);
                    }
                }
                return CompoundQueryExpression.or(expressions);
            case AND:
                return CompoundQueryExpression.and(partial, expressions);
            default:
                String message = String.format(Locale.ROOT, "Unable to handle call: [%s]", call);
                throw new PredicateAnalyzerException(message);
        }
    }

    /**
     * Holder class for a pair of expressions. Used to convert {@code 1 = foo} into {@code foo = 1}
     */
    private static class SwapResult {
        final boolean swapped;
        final TerminalExpression terminal;
        final LiteralExpression literal;

        SwapResult(boolean swapped, TerminalExpression terminal, LiteralExpression literal) {
            super();
            this.swapped = swapped;
            this.terminal = terminal;
            this.literal = literal;
        }

        TerminalExpression getKey() {
            return terminal;
        }

        LiteralExpression getValue() {
            return literal;
        }

        boolean isSwapped() {
            return swapped;
        }
    }

    /**
     * Swap order of operands such that the literal expression is always on the right.
     *
     * <p>NOTE: Some combinations of operands are implicitly not supported and will
     * cause an exception to be thrown. For example, we currently do not support
     * comparing a literal to another literal as convention {@code 5 = 5}. Nor do we support
     * comparing named fields to other named fields as convention {@code $0 = $1}.
     * @param left left expression
     * @param right right expression
     */
    private static SwapResult swap(Expression left, Expression right) {

        TerminalExpression terminal;
        LiteralExpression literal = expressAsLiteral(left);
        boolean swapped = false;
        if (literal != null) {
            swapped = true;
            terminal = (TerminalExpression) right;
        } else {
            literal = expressAsLiteral(right);
            terminal = (TerminalExpression) left;
        }

        if (literal == null || terminal == null) {
            String message = String.format(Locale.ROOT,
                    "Unexpected combination of expressions [left: %s] [right: %s]", left, right);
            throw new PredicateAnalyzerException(message);
        }

        if (CastExpression.isCastExpression(terminal)) {
            terminal = CastExpression.unpack(terminal);
        }

        return new SwapResult(swapped, terminal, literal);
    }

    private CastExpression toCastExpression(RexCall call) {
        TerminalExpression argument = (TerminalExpression) call.getOperands().get(0).accept(this);
        return new CastExpression(call.getType(), argument);
    }

    private static NamedFieldExpression toNamedField(RexLiteral literal) {
        return new NamedFieldExpression(literal);
    }

    /**
     * Try to convert a generic expression into a literal expression.
     */
    private static LiteralExpression expressAsLiteral(Expression exp) {

        if (exp instanceof LiteralExpression) {
            return (LiteralExpression) exp;
        }

        return null;
    }

    private static boolean isColumn(Expression exp, RexNode node,
                                    String columnName, boolean throwException) {
        if (!(exp instanceof NamedFieldExpression)) {
            return false;
        }

        final NamedFieldExpression termExp = (NamedFieldExpression) exp;
        if (columnName.equals(termExp.getRootName())) {
            if (throwException) {
                throw new PredicateAnalyzerException("Cannot handle _id field in " + node);
            }
            return true;
        }
        return false;
    }

    /**
     * If one operand in a binary operator is a DateTime type, but the other isn't,
     * we should not push down the predicate.
     *
     * @param call Current node being evaluated
     */
    private static void checkForIncompatibleDateTimeOperands(RexCall call) {
        RelDataType op1 = call.getOperands().get(0).getType();
        RelDataType op2 = call.getOperands().get(1).getType();
        if ((SqlTypeFamily.DATETIME.contains(op1) && !SqlTypeFamily.DATETIME.contains(op2))
                || (SqlTypeFamily.DATETIME.contains(op2) && !SqlTypeFamily.DATETIME.contains(op1))
                || (SqlTypeFamily.DATE.contains(op1) && !SqlTypeFamily.DATE.contains(op2))
                || (SqlTypeFamily.DATE.contains(op2) && !SqlTypeFamily.DATE.contains(op1))
                || (SqlTypeFamily.TIMESTAMP.contains(op1) && !SqlTypeFamily.TIMESTAMP.contains(op2))
                || (SqlTypeFamily.TIMESTAMP.contains(op2) && !SqlTypeFamily.TIMESTAMP.contains(op1))
                || (SqlTypeFamily.TIME.contains(op1) && !SqlTypeFamily.TIME.contains(op2))
                || (SqlTypeFamily.TIME.contains(op2) && !SqlTypeFamily.TIME.contains(op1))) {
            throw new PredicateAnalyzerException("Cannot handle " + call.getKind()
                    + " expression for _id field, " + call);
        }
    }
}
