package com.owl.web.model.monitor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.owl.web.dao.entity.TbHistory;
import com.owl.web.model.CommonFilter;

public class HistoryFilterReq extends CommonFilter<TbHistory> {
    private Long timeFrom;
    private Long timeTo;
    private String guid;
    private String metric;
    private String instance;
    private String host;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private String integrationName;

    @Override
    public QueryWrapper<TbHistory> where(QueryWrapper<TbHistory> wrapper) {
        wrapper.lambda()
                .ge(timeFrom != null, TbHistory::getTime, timeFrom)
                .le(timeTo != null, TbHistory::getTime, timeTo)
                .eq(StrUtil.isNotEmpty(guid), TbHistory::getGuid, guid)
                .eq(StrUtil.isNotEmpty(metric), TbHistory::getMetric, metric)
                .eq(StrUtil.isNotEmpty(instance), TbHistory::getInstance, instance)
                .eq(StrUtil.isNotEmpty(host), TbHistory::getHost, host)
                .eq(StrUtil.isNotEmpty(tag1), TbHistory::getTag1, tag1)
                .eq(StrUtil.isNotEmpty(tag2), TbHistory::getTag2, tag2)
                .eq(StrUtil.isNotEmpty(tag3), TbHistory::getTag3, tag3)
                .eq(StrUtil.isNotEmpty(tag4), TbHistory::getTag4, tag4)
                .eq(StrUtil.isNotEmpty(tag5), TbHistory::getTag5, tag5);
        return wrapper;
    }

    public Long getTimeFrom() {
        return timeFrom;
    }

    public HistoryFilterReq setTimeFrom(Long timeFrom) {
        this.timeFrom = timeFrom;
        return this;
    }

    public Long getTimeTo() {
        return timeTo;
    }

    public HistoryFilterReq setTimeTo(Long timeTo) {
        this.timeTo = timeTo;
        return this;
    }

    public String getGuid() {
        return guid;
    }

    public HistoryFilterReq setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public String getMetric() {
        return metric;
    }

    public HistoryFilterReq setMetric(String metric) {
        this.metric = metric;
        return this;
    }

    public String getInstance() {
        return instance;
    }

    public HistoryFilterReq setInstance(String instance) {
        this.instance = instance;
        return this;
    }

    public String getHost() {
        return host;
    }

    public HistoryFilterReq setHost(String host) {
        this.host = host;
        return this;
    }

    public String getTag1() {
        return tag1;
    }

    public HistoryFilterReq setTag1(String tag1) {
        this.tag1 = tag1;
        return this;
    }

    public String getTag2() {
        return tag2;
    }

    public HistoryFilterReq setTag2(String tag2) {
        this.tag2 = tag2;
        return this;
    }

    public String getTag3() {
        return tag3;
    }

    public HistoryFilterReq setTag3(String tag3) {
        this.tag3 = tag3;
        return this;
    }

    public String getTag4() {
        return tag4;
    }

    public HistoryFilterReq setTag4(String tag4) {
        this.tag4 = tag4;
        return this;
    }

    public String getTag5() {
        return tag5;
    }

    public HistoryFilterReq setTag5(String tag5) {
        this.tag5 = tag5;
        return this;
    }

    public String getIntegrationName() {
        return integrationName;
    }

    public HistoryFilterReq setIntegrationName(String integrationName) {
        this.integrationName = integrationName;
        return this;
    }
}
