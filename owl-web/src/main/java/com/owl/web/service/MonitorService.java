package com.owl.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.owl.api.monitor.Data;
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

import java.util.stream.Collectors;

@Service
public class MonitorService {
    @Autowired
    private TbIntegrationService tbIntegrationService;
    @Autowired
    private TbHistoryService tbHistoryService;
    @Autowired
    private IntegrationPoolService integrationPoolService;

    public History[] metric(String name) {
        TbIntegration entity = tbIntegrationService.exists(name);
        Data[] data = integrationPoolService.metric(entity);
        History[] result = new History[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = new History().copyFrom(data[i])
                    .setIntegrationName(entity.getName());
        }
        return result;
    }

    public Paged<History> history(HistoryQueryReq req) {
        IPage<TbHistory> paged = tbHistoryService.page(req.page(), req.query());
        long total = paged.getTotal();
        return new Paged<>(total, paged.getRecords().stream().map(i ->
            new History().copyFrom(i)
        ).collect(Collectors.toList()));
    }
}
