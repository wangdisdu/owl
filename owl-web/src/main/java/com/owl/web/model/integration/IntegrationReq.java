package com.owl.web.model.integration;

import com.owl.common.JsonUtil;
import com.owl.web.dao.entity.TbIntegration;
import com.owl.web.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
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
}
