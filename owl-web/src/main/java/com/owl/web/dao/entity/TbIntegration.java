package com.owl.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName
public class TbIntegration {

    @TableId
    private String name;

    @TableField
    private String description;

    @TableField
    private String config;

    @TableField
    private String builder;

    @TableField
    private String meta;

    @TableField
    private Long createTime;

    @TableField
    private String createBy;

    @TableField
    private Long updateTime;

    @TableField
    private String updateBy;

    public String getName() {
        return name;
    }

    public TbIntegration setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TbIntegration setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getConfig() {
        return config;
    }

    public TbIntegration setConfig(String config) {
        this.config = config;
        return this;
    }

    public String getBuilder() {
        return builder;
    }

    public TbIntegration setBuilder(String builder) {
        this.builder = builder;
        return this;
    }

    public String getMeta() {
        return meta;
    }

    public TbIntegration setMeta(String meta) {
        this.meta = meta;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public TbIntegration setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public TbIntegration setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public TbIntegration setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public TbIntegration setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }
}