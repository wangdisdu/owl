package com.owl.integration.elasticsearch.client.request.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-constant-score-query.html
 */
public class ConstantScoreQuery extends Query {
    protected Double boost;
    protected Query filter;

    public ConstantScoreQuery setQuery(Query query) {
        this.filter = query;
        return this;
    }

    public ConstantScoreQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public Double getBoost() {
        return boost;
    }

    public Query getFilter() {
        return filter;
    }

    // "constant_score" : {
    //     "filter" : {
    //         ....
    //     },
    //     "boost" : 1.5
    // }
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        if(boost != null) {
            params.put("boost", boost);
        }
        params.put("filter", filter.serialize());
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("constant_score", params);
        return query;
    }
}
