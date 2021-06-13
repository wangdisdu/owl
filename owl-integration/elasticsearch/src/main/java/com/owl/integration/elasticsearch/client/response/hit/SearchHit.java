package com.owl.integration.elasticsearch.client.response.hit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.owl.common.JsonUtil;
import com.owl.integration.elasticsearch.client.ResponseDeserializer;
import com.owl.integration.elasticsearch.client.request.SearchRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SearchHit implements ResponseDeserializer {
    public static final String SCORE = "_score";
    public static final String INDEX = "_index";
    public static final String TYPE = "_type";
    public static final String ID = "_id";
    public static final String VERSION = "_version";
    public static final String SOURCE = "_source";
    public static final String FIELDS = "fields";
    public static final String HIGHLIGHT = "highlight";

    protected SearchRequest search;

    protected Double score;
    protected String index;
    protected String type;
    protected String id;
    protected Long version;
    protected Map<String, Object> source;
    protected Map<String, List<Object>> fields;
    protected Map<String, List<String>> highlight;

    public SearchHit(SearchRequest search) {
        this.search = search;
    }

    public Double getScore() {
        return score;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public Map<String, Object> getSource() {
        return source;
    }

    public Map<String, List<Object>> getFields() {
        return fields;
    }

    public Map<String, List<String>> getHighlight() {
        return highlight;
    }

    @Override
    public SearchHit deserialize(JsonNode json) throws IOException {
        if (json.has(SCORE)) this.score = json.get(SCORE).asDouble();
        if (json.has(INDEX)) this.index = json.get(INDEX).asText();
        if (json.has(TYPE)) this.type = json.get(TYPE).asText();
        if (json.has(ID)) this.id = json.get(ID).asText();
        if (json.has(VERSION)) this.version = json.get(VERSION).asLong();
        if (json.has(SOURCE)) {
            this.source = JsonUtil.decode2Map(json.get(SOURCE));
        }
        if (json.has(FIELDS)) {
            this.fields = JsonUtil.decode(json.get(FIELDS), new TypeReference<Map<String, List<Object>>>(){});
        }
        if (json.has(HIGHLIGHT)) {
            this.highlight = JsonUtil.decode(json.get(HIGHLIGHT), new TypeReference<Map<String, List<String>>>(){});
        }
        return this;
    }
}
