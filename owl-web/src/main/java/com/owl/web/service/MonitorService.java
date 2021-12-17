package com.owl.web.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.owl.api.monitor.Metric;
import com.owl.web.dao.entity.TbHistory;
import com.owl.web.dao.entity.TbIntegration;
import com.owl.web.dao.service.TbHistoryService;
import com.owl.web.dao.service.TbIntegrationService;
import com.owl.web.model.Paged;
import com.owl.web.model.monitor.History;
import com.owl.web.model.monitor.HistoryQueryReq;
import com.owl.web.provider.IntegrationPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitorService {
    @Autowired
    private TbIntegrationService tbIntegrationService;
    @Autowired
    private TbHistoryService tbHistoryService;
    @Autowired
    private IntegrationPoolService integrationPoolService;

    public Paged<History> metricLast(HistoryQueryReq req) {
        TbIntegration entity = tbIntegrationService.exists(req.getFilter().getIntegrationName());
        Metric[] data = integrationPoolService.metricLast(entity);

        List<History> result = new ArrayList<>();
        for (Metric datum : data) {
            History item = new History().copyFrom(datum);
            boolean filtered = (StrUtil.isEmpty(req.getFilter().getGuid()) || StrUtil.equals(req.getFilter().getGuid(), item.getGuid()))
                    && (StrUtil.isEmpty(req.getFilter().getMetric()) || StrUtil.equals(req.getFilter().getMetric(), item.getMetric()))
                    && (StrUtil.isEmpty(req.getFilter().getInstance()) || StrUtil.equals(req.getFilter().getInstance(), item.getInstance()))
                    && (StrUtil.isEmpty(req.getFilter().getCategory()) || StrUtil.equals(req.getFilter().getCategory(), item.getCategory()))
                    && (StrUtil.isEmpty(req.getFilter().getHost()) || StrUtil.equals(req.getFilter().getHost(), item.getHost()))
                    && (StrUtil.isEmpty(req.getFilter().getTag1()) || StrUtil.equals(req.getFilter().getTag1(), item.getTag1()))
                    && (StrUtil.isEmpty(req.getFilter().getTag2()) || StrUtil.equals(req.getFilter().getTag2(), item.getTag2()))
                    && (StrUtil.isEmpty(req.getFilter().getTag3()) || StrUtil.equals(req.getFilter().getTag3(), item.getTag3()))
                    && (StrUtil.isEmpty(req.getFilter().getTag4()) || StrUtil.equals(req.getFilter().getTag4(), item.getTag4()))
                    && (StrUtil.isEmpty(req.getFilter().getTag5()) || StrUtil.equals(req.getFilter().getTag5(), item.getTag5()))
                    && (req.getFilter().getValueGt() == null || item.getValue() > req.getFilter().getValueGt())
                    && (req.getFilter().getValueGte() == null || item.getValue() >= req.getFilter().getValueGte())
                    && (req.getFilter().getValueLt() == null || item.getValue() < req.getFilter().getValueLt())
                    && (req.getFilter().getValueLte() == null || item.getValue() <= req.getFilter().getValueLte());
            if (filtered) {
                result.add(item);
            }
        }
        if (StrUtil.isNotEmpty(req.getSorter().getKey())) {
            result = result.stream().sorted((i1, i2) -> {
                if ("guid".equals(req.getSorter().getKey())) {
                    return StrUtil.compare(i1.getGuid(), i2.getGuid(), false);
                } else if ("metric".equals(req.getSorter().getKey())) {
                    return StrUtil.compare(i1.getMetric(), i2.getMetric(), false);
                } else if ("instance".equals(req.getSorter().getKey())) {
                    return StrUtil.compare(i1.getInstance(), i2.getInstance(), false);
                } else if ("category".equals(req.getSorter().getKey())) {
                    return StrUtil.compare(i1.getCategory(), i2.getCategory(), false);
                } else if ("host".equals(req.getSorter().getKey())) {
                    return StrUtil.compare(i1.getHost(), i2.getHost(), false);
                } else if ("tag1".equals(req.getSorter().getKey())) {
                    return StrUtil.compare(i1.getTag1(), i2.getTag1(), false);
                } else if ("tag2".equals(req.getSorter().getKey())) {
                    return StrUtil.compare(i1.getTag2(), i2.getTag2(), false);
                } else if ("tag3".equals(req.getSorter().getKey())) {
                    return StrUtil.compare(i1.getTag3(), i2.getTag3(), false);
                } else if ("tag4".equals(req.getSorter().getKey())) {
                    return StrUtil.compare(i1.getTag4(), i2.getTag4(), false);
                } else if ("tag5".equals(req.getSorter().getKey())) {
                    return StrUtil.compare(i1.getTag5(), i2.getTag5(), false);
                } else if ("value".equals(req.getSorter().getKey())) {
                    return Double.compare(i1.getValue(), i2.getValue());
                }
                return 0;
            }).collect(Collectors.toList());
            if (!Boolean.TRUE.equals(req.getSorter().getAsc())) {
                Collections.reverse(result);
            }
        }

        int total = result.size();
        int from = 0;
        int to = total;
        if (req.getPage() != null && req.getSize() != null) {
            from = req.getPage() * req.getSize();
            to = Math.min(from + req.getSize(), total);
        }
        return new Paged<>(total, result.subList(from, to));
    }

    public Paged<History> history(HistoryQueryReq req) {
        IPage<TbHistory> paged = tbHistoryService.page(req.page(), req.query());
        long total = paged.getTotal();
        return new Paged<>(total, paged.getRecords().stream().map(i ->
            new History().copyFrom(i)
        ).collect(Collectors.toList()));
    }
}
