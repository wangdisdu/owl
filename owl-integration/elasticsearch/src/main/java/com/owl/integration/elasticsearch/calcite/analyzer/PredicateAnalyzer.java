package com.owl.integration.elasticsearch.calcite.analyzer;

import com.google.common.base.Throwables;
import com.owl.integration.elasticsearch.client.request.query.Query;
import org.apache.calcite.rex.RexNode;

import java.util.List;
import java.util.Objects;

/**
 * Query predicate analyzer. Uses visitor pattern to traverse existing expression
 * and convert it to ES {@link Query}.
 *
 * <p>Major part of this class have been copied from
 * <a href="https://www.dremio.com/">dremio</a> ES adapter
 * (thanks to their team for improving calcite-ES integration).
 */
public class PredicateAnalyzer {

    private PredicateAnalyzer() {}

    /**
     * Walks the expression tree, attempting to convert the entire tree into
     * an equivalent Elasticsearch query filter. If an error occurs, or if it
     * is determined that the expression cannot be converted, an exception is
     * thrown and an error message logged.
     *
     * <p>Callers should catch ExpressionNotAnalyzableException
     * and fall back to not using push-down filters.
     *
     * @param expression expression to analyze
     * @param fields input fields
     * @return search query which can be used to query ES cluster
     * @throws ExpressionNotAnalyzableException when expression can't processed by this analyzer
     */
    public static Query analyze(RexNode expression, List<String> fields) throws ExpressionNotAnalyzableException {
        Objects.requireNonNull(expression, "expression");
        try {
            // visits expression tree
            QueryExpression e = (QueryExpression) expression.accept(new PredicateVisitor(fields));

            if (e != null && e.isPartial()) {
                throw new UnsupportedOperationException("Can't handle partial QueryExpression: " + e);
            }
            return e != null ? e.query() : null;
        } catch (Throwable e) {
            Throwables.propagateIfPossible(e, UnsupportedOperationException.class);
            throw new ExpressionNotAnalyzableException("Can't convert " + expression, e);
        }
    }

}
