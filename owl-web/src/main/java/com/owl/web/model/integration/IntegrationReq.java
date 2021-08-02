package com.owl.web.model.integration;

import com.owl.common.JsonUtil;
import com.owl.web.dao.entity.TbIntegration;
import com.owl.web.model.BaseModel;

import java.util.Map;

public class IntegrationReq implements BaseModel<TbIntegration> {
    private String name;
    private String description;
    private Map<String, Object> config;
    private String builder;
    private Map<String, Object> meta;

    public TbIntegration to(TbIntegration entity) {
        copyTo(entity);
        entity.setConfig(JsonUtil.encodeUnSafe(config));
        entity.setMeta(JsonUtil.encodeUnSafe(meta));
        return entity;
    }

    public String getName() {
        return name;
    }

    public IntegrationReq setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IntegrationReq setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public IntegrationReq setConfig(Map<String, Object> config) {
        this.config = config;
        return this;
    }

    public String getBuilder() {
        return builder;
    }

    public IntegrationReq setBuilder(String builder) {
        this.builder = builder;
        return this;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public IntegrationReq setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }
}
