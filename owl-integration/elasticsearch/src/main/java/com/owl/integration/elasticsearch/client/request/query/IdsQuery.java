package com.owl.integration.elasticsearch.client.request.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-ids-query.html
 */
public class IdsQuery extends Query {
    protected List<String> types = new ArrayList<>();
    protected List<String> values = new ArrayList<>();
    protected Double boost;
    protected String queryName;

    public IdsQuery setTypes(List<String> types) {
        this.types = types;
        return this;
    }

    public IdsQuery addType(String type) {
        this.types.add(type);
        return this;
    }

    public IdsQuery setIds(List<String> values) {
        this.values = values;
        return this;
    }

    public IdsQuery addId(String value) {
        this.values.add(value);
        return this;
    }

    public IdsQuery setBoost(Double boost) {
        this.boost = boost;
        return this;
    }

    public IdsQuery setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getValues() {
        return values;
    }

    public Double getBoost() {
        return boost;
    }

    public String getQueryName() {
        return queryName;
    }

    // {
    //  "ids" : {
    //      "type" : "my_type",
    //      "values" : ["1", "4", "100"]
    //  }
    // }
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> params = new LinkedHashMap<>();
        if (types.size() == 1) {
            params.put("type", types.get(0));
        } else if (types.size() > 1) {
            params.put("type", types);
        }
        params.put("values", values);
        if (this.boost != null) {
            params.put("boost", this.boost);
        }
        if (this.queryName != null) {
            params.put("_name", this.queryName);
        }
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("ids", params);
        return query;
    }
}
