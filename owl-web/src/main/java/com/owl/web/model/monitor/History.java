package com.owl.web.model.monitor;

import com.owl.api.monitor.Data;
import com.owl.web.dao.entity.TbHistory;
import org.springframework.beans.BeanUtils;

public class History {
    private Long time;
    private Double value;
    private String guid;
    private String metric;
    private String instance;
    private String category;
    private String host;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private String alias;
    private String unit;
    private String integrationName;

    public History copyFrom(TbHistory entity) {
        BeanUtils.copyProperties(entity, this);
        return this;
    }

    public History copyFrom(Data data) {
        BeanUtils.copyProperties(data, this);
        return this;
    }

    public TbHistory copyTo(TbHistory entity) {
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

    public Long getTime() {
        return time;
    }

    public History setTime(Long time) {
        this.time = time;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public History setValue(Double value) {
        this.value = value;
        return this;
    }

    public String getGuid() {
        return guid;
    }

    public History setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public String getMetric() {
        return metric;
    }

    public History setMetric(String metric) {
        this.metric = metric;
        return this;
    }

    public String getInstance() {
        return instance;
    }

    public History setInstance(String instance) {
        this.instance = instance;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public History setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getHost() {
        return host;
    }

    public History setHost(String host) {
        this.host = host;
        return this;
    }

    public String getTag1() {
        return tag1;
    }

    public History setTag1(String tag1) {
        this.tag1 = tag1;
        return this;
    }

    public String getTag2() {
        return tag2;
    }

    public History setTag2(String tag2) {
        this.tag2 = tag2;
        return this;
    }

    public String getTag3() {
        return tag3;
    }

    public History setTag3(String tag3) {
        this.tag3 = tag3;
        return this;
    }

    public String getTag4() {
        return tag4;
    }

    public History setTag4(String tag4) {
        this.tag4 = tag4;
        return this;
    }

    public String getTag5() {
        return tag5;
    }

    public History setTag5(String tag5) {
        this.tag5 = tag5;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public History setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public History setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String getIntegrationName() {
        return integrationName;
    }

    public History setIntegrationName(String integrationName) {
        this.integrationName = integrationName;
        return this;
    }
}
