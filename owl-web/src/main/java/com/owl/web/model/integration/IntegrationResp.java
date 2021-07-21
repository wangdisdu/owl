package com.owl.web.model.integration;

import cn.hutool.core.util.StrUtil;
import com.owl.common.JsonUtil;
import com.owl.web.dao.entity.TbIntegration;
import com.owl.web.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
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
}
