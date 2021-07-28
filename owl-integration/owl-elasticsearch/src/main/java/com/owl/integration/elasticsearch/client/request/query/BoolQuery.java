package com.owl.integration.elasticsearch.client.request.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-bool-query.html
 */
public class BoolQuery extends Query {
    protected List<Query> mustClauses = new ArrayList<>();
    protected List<Query> mustNotClauses = new ArrayList<>();
    protected List<Query> filterClauses = new ArrayList<>();
    protected List<Query> shouldClauses = new ArrayList<>();
    protected String minimumShouldMatch;
    protected Double boost;
    protected String queryName;

    public BoolQuery addMust(Query query) {
        this.mustClauses.add(query);
        return this;
    }

    public BoolQuery setMusts(List<Query> queries) {
        this.mustClauses = queries;
        return this;
    }

    public BoolQuery addMustNot(Query query) {
        this.mustNotClauses.add(query);
        return this;
    }

    public BoolQuery setMustNots(List<Query> queries) {
        this.mustNotClauses = queries;
        return this;
    }

    public BoolQuery addFilter(Query query) {
        this.filterClauses.add(query);
        return this;
    }

    public BoolQuery setFilters(List<Query> queries) {
        this.filterClauses = queries;
        return this;
    }

    public BoolQuery addShould(Query query) {
        this.shouldClauses.add(query);
        return this;
    }

    public BoolQuery setShoulds(List<Query> queries) {
        this.shouldClauses = queries;
        return this;
    }

    public BoolQuery setMinimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    public BoolQuery setMinimumShouldMatch(int minimumShouldMatch) {
        return setMinimumShouldMatch(String.valueOf(minimumShouldMatch));
    }

    public BoolQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public BoolQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    private void appendClauses(Map<String, Object> bool, String key, List<Query> queries) {
        if (queries.size() == 1) {
            bool.put(key, queries.get(0).serialize());
        } else if (queries.size() > 1) {
            List<Map<String, Object>> clauses = new ArrayList<>();
            for (Query query : queries) {
                clauses.add(query.serialize());
            }
            bool.put(key, clauses);
        }
    }

    public List<Query> getMustClauses() {
        return mustClauses;
    }

    public List<Query> getMustNotClauses() {
        return mustNotClauses;
    }

    public List<Query> getFilterClauses() {
        return filterClauses;
    }

    public List<Query> getShouldClauses() {
        return shouldClauses;
    }

    public String getMinimumShouldMatch() {
        return minimumShouldMatch;
    }

    public Double getBoost() {
        return boost;
    }

    public String getQueryName() {
        return queryName;
    }

    /*{
            "bool":{
                "must":{
                    "term":{
                        "user":"kimchy"
                    }
                },
                "must_not":{
                    "range":{
                        "age":{
                            "from":10,
                            "to":20
                        }
                    }
                },
                "filter":[],
                "should":[
                    {
                        "term":{
                            "tag":"wow"
                        }
                    },
                    {
                        "term":{
                            "tag":"elasticsearch"
                        }
                    }
                ],
                "minimum_should_match":1,
                "boost":1
            }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> bool = new LinkedHashMap<>();
        // must
        this.appendClauses(bool, "must", this.mustClauses);
        // must_not
        this.appendClauses(bool, "must_not", this.mustNotClauses);
        // filter
        this.appendClauses(bool, "filter", this.filterClauses);
        // should
        this.appendClauses(bool, "should", this.shouldClauses);

        if (this.minimumShouldMatch != null) {
            bool.put("minimum_should_match", this.minimumShouldMatch);
        }
        if (this.boost != null) {
            bool.put("boost", this.boost);
        }
        if (this.queryName != null) {
            bool.put("_name", this.queryName);
        }

        Map<String, Object> query = new LinkedHashMap<>();
        query.put("bool", bool);
        return query;
    }
}
