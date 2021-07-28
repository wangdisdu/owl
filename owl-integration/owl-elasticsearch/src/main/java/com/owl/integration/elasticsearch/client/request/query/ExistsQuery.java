package com.owl.integration.elasticsearch.client.request.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-exists-query.html
 */
public class ExistsQuery extends Query {
    protected String field;
    protected String queryName;

    public ExistsQuery setField(String field) {
        this.field = field;
        return this;
    }

    public ExistsQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public String getField() {
        return field;
    }

    public String getQueryName() {
        return queryName;
    }

    /*{
            "exists" : { "field" : "user" }
        }*/
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("field", this.field);
        if (this.queryName != null) {
            params.put("_name", this.queryName);
        }
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("exists", params);
        return query;
    }
}
