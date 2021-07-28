package com.owl.integration.elasticsearch.client.request;

import cn.hutool.core.collection.CollUtil;
import com.owl.integration.elasticsearch.client.request.aggregation.Aggregation;
import com.owl.integration.elasticsearch.client.request.query.Query;
import com.owl.integration.elasticsearch.client.request.script.Script;
import com.owl.integration.elasticsearch.client.request.sorter.Sorter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchRequest implements Serializable {
    protected Integer from;
    protected Integer size;
    protected Query query;
    protected Map<String, Aggregation> aggregations = new LinkedHashMap<>();
    protected List<Sorter> sorters = new ArrayList<>();
    protected List<String> includeFields = new ArrayList<>();
    protected List<String> excludeFields = new ArrayList<>();
    protected Map<String, Script> scriptFields = new LinkedHashMap<>();

    public Integer getFrom() {
        return from;
    }

    public SearchRequest setFrom(Integer from) {
        this.from = from;
        return this;
    }

    public Integer getSize() {
        return size;
    }

    public SearchRequest setSize(Integer size) {
        this.size = size;
        return this;
    }

    public Query getQuery() {
        return query;
    }

    public SearchRequest setQuery(Query query) {
        this.query = query;
        return this;
    }

    public Map<String, Aggregation> getAggregations() {
        return aggregations;
    }

    public SearchRequest setAggregations(Map<String, Aggregation> aggregations) {
        this.aggregations = aggregations;
        return this;
    }

    public SearchRequest addAggregation(String name, Aggregation aggregation) {
        this.aggregations.put(name, aggregation);
        return this;
    }

    public List<Sorter> getSorters() {
        return sorters;
    }

    public SearchRequest setSorters(List<Sorter> sorters) {
        this.sorters = sorters;
        return this;
    }

    public SearchRequest addSort(Sorter sorter) {
        this.sorters.add(sorter);
        return this;
    }

    public List<String> getIncludeFields() {
        return includeFields;
    }

    public SearchRequest setIncludeFields(List<String> includeFields) {
        this.includeFields = includeFields;
        return this;
    }

    public SearchRequest addIncludeField(String include) {
        this.includeFields.add(include);
        return this;
    }

    public List<String> getExcludeFields() {
        return excludeFields;
    }

    public SearchRequest setExcludeFields(List<String> excludeFields) {
        this.excludeFields = excludeFields;
        return this;
    }

    public SearchRequest addExcludeField(String exclude) {
        this.excludeFields.add(exclude);
        return this;
    }

    public Map<String, Script> getScriptFields() {
        return scriptFields;
    }

    public SearchRequest setScriptFields(Map<String, Script> scriptFields) {
        this.scriptFields = scriptFields;
        return this;
    }

    public SearchRequest addScriptField(String field, Script script) {
        this.scriptFields.put(field, script);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> source = new LinkedHashMap<>();
        if (this.query != null) {
            source.put("query", this.query.serialize());
        }
        if (this.from != null) {
            source.put("from", this.from);
        }
        if (this.size != null) {
            source.put("size", this.size);
        }
        if (CollUtil.isNotEmpty(this.sorters)) {
            List<Map<String, Object>> sorts = new ArrayList<>();
            for (Sorter s : this.sorters) {
                sorts.add(s.serialize());
            }
            source.put("sort", sorts);
        }
        if (CollUtil.isNotEmpty(this.includeFields) && CollUtil.isNotEmpty(this.excludeFields)) {
            Map<String, Object> param = new LinkedHashMap<>();
            param.put("includes", this.includeFields);
            param.put("excludes", this.excludeFields);
            source.put("_source", param);
        } else if (CollUtil.isNotEmpty(this.excludeFields)) {
            Map<String, Object> param = new LinkedHashMap<>();
            param.put("excludes", this.excludeFields);
            source.put("_source", param);
        } else if (CollUtil.isNotEmpty(this.includeFields)) {
            source.put("_source", this.includeFields);
        }
        if (this.scriptFields.size() > 0) {
            Map<String, Object> param = new LinkedHashMap<>();
            for (Map.Entry<String, Script> e : this.scriptFields.entrySet()) {
                Map<String, Object> script = new LinkedHashMap<>();
                script.put("script", e.getValue().serialize());
                param.put(e.getKey(), script);
            }
            source.put("script_fields", param);
        }
        if (CollUtil.isNotEmpty(this.aggregations)) {
            Map<String, Object> param = new LinkedHashMap<>();
            for (Map.Entry<String, Aggregation> e : this.aggregations.entrySet()) {
                param.put(e.getKey(), e.getValue().serialize());
            }
            source.put("aggregations", param);
        }
        return source;
    }
}
