package com.owl.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;

public class TbHistory {
    @TableField
    private Long time;
    @TableField
    private Double value;
    @TableField
    private String guid;
    @TableField
    private String metric;
    @TableField
    private String instance;
    @TableField
    private String category;
    @TableField
    private String host;
    @TableField
    private String tag1;
    @TableField
    private String tag2;
    @TableField
    private String tag3;
    @TableField
    private String tag4;
    @TableField
    private String tag5;
    @TableField
    private String alias;
    @TableField
    private String unit;
    @TableField
    private String integrationName;

    public Long getTime() {
        return time;
    }

    public TbHistory setTime(Long time) {
        this.time = time;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public TbHistory setValue(Double value) {
        this.value = value;
        return this;
    }

    public String getGuid() {
        return guid;
    }

    public TbHistory setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public String getMetric() {
        return metric;
    }

    public TbHistory setMetric(String metric) {
        this.metric = metric;
        return this;
    }

    public String getInstance() {
        return instance;
    }

    public TbHistory setInstance(String instance) {
        this.instance = instance;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public TbHistory setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getHost() {
        return host;
    }

    public TbHistory setHost(String host) {
        this.host = host;
        return this;
    }

    public String getTag1() {
        return tag1;
    }

    public TbHistory setTag1(String tag1) {
        this.tag1 = tag1;
        return this;
    }

    public String getTag2() {
        return tag2;
    }

    public TbHistory setTag2(String tag2) {
        this.tag2 = tag2;
        return this;
    }

    public String getTag3() {
        return tag3;
    }

    public TbHistory setTag3(String tag3) {
        this.tag3 = tag3;
        return this;
    }

    public String getTag4() {
        return tag4;
    }

    public TbHistory setTag4(String tag4) {
        this.tag4 = tag4;
        return this;
    }

    public String getTag5() {
        return tag5;
    }

    public TbHistory setTag5(String tag5) {
        this.tag5 = tag5;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public TbHistory setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public TbHistory setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String getIntegrationName() {
        return integrationName;
    }

    public TbHistory setIntegrationName(String integrationName) {
        this.integrationName = integrationName;
        return this;
    }
}
