package com.owl.web.model.integration;

import cn.hutool.core.util.StrUtil;
import com.owl.common.JsonUtil;
import com.owl.web.dao.entity.TbIntegration;
import com.owl.web.model.BaseModel;

import java.util.Map;

public class IntegrationResp implements BaseModel<TbIntegration> {
    private String name;
    private String description;
    private Map<String, Object> config;
    private String builder;
    private Map<String, Object> meta;
    private Long createTime;
    private String createBy;
    private Long updateTime;
    private String updateBy;

    public IntegrationResp from(TbIntegration entity) {
        copyFrom(entity);
        setConfig(JsonUtil.decode2MapUnSafe(entity.getConfig()));
        setMeta(JsonUtil.decode2MapUnSafe(entity.getMeta()));
        if (StrUtil.isNotBlank((String) getMeta().get("icon"))) {
            getMeta().put("iconUrl", "/api/v1/integration/builder/" + builder + "/icon");
        } else {
            getMeta().put("iconUrl", "");
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public IntegrationResp setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IntegrationResp setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public IntegrationResp setConfig(Map<String, Object> config) {
        this.config = config;
        return this;
    }

    public String getBuilder() {
        return builder;
    }

    public IntegrationResp setBuilder(String builder) {
        this.builder = builder;
        return this;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public IntegrationResp setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public IntegrationResp setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public IntegrationResp setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public IntegrationResp setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public IntegrationResp setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }
}
