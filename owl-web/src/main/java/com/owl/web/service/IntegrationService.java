package com.owl.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.owl.api.annotation.IntegrationMeta;
import com.owl.api.schema.DataFrame;
import com.owl.api.schema.IntegrationSchema;
import com.owl.web.common.ContextHolder;
import com.owl.web.dao.entity.TbHistory;
import com.owl.web.dao.entity.TbIntegration;
import com.owl.web.dao.service.TbHistoryService;
import com.owl.web.dao.service.TbIntegrationService;
import com.owl.web.model.integration.IntegrationQuery;
import com.owl.web.model.integration.IntegrationReq;
import com.owl.web.model.integration.IntegrationResp;
import com.owl.web.provider.IntegrationBuilderService;
import com.owl.web.provider.IntegrationPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IntegrationService {
    @Autowired
    private TbIntegrationService tbIntegrationService;
    @Autowired
    private TbHistoryService tbHistoryService;
    @Autowired
    private IntegrationPoolService integrationPoolService;
    @Autowired
    private IntegrationBuilderService integrationBuilderService;

    public List<IntegrationMeta> builders() {
        return integrationBuilderService.scan();
    }

    public byte[] icon(String builder) {
        return integrationBuilderService.icon(builder);
    }

    public List<IntegrationResp> list() {
        return tbIntegrationService.list().stream()
                .map(i -> new IntegrationResp().from(i))
                .collect(Collectors.toList());
    }

    public IntegrationResp get(String name) {
        TbIntegration entity = tbIntegrationService.exists(name);
        return new IntegrationResp().from(entity);
    }

    public IntegrationResp delete(String name) {
        IntegrationResp resp = get(name);
        clear(name);
        return resp;
    }

    public IntegrationResp create(IntegrationReq req) {
        tbIntegrationService.notExists(req.getName());
        long now = System.currentTimeMillis();
        TbIntegration entity = req.to(new TbIntegration());
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setCreateBy(ContextHolder.loginAccount());
        entity.setUpdateBy(ContextHolder.loginAccount());
        tbIntegrationService.save(entity);
        return get(req.getName());
    }

    public IntegrationResp update(IntegrationReq req) {
        TbIntegration entity = tbIntegrationService.exists(req.getName());
        long now = System.currentTimeMillis();
        req.to(entity);
        entity.setUpdateTime(now);
        entity.setUpdateBy(ContextHolder.loginAccount());
        tbIntegrationService.updateById(entity);
        return get(req.getName());
    }

    public DataFrame query(IntegrationQuery req) {
        TbIntegration entity = tbIntegrationService.exists(req.getName());
        return integrationPoolService.query(entity, req);
    }

    public IntegrationSchema schema(String name) {
        TbIntegration entity = tbIntegrationService.exists(name);
        return integrationPoolService.schema(entity);
    }

    public void clear(String name) {
        tbHistoryService.remove(
                new LambdaQueryWrapper<TbHistory>()
                        .eq(TbHistory::getIntegrationName, name));
        tbIntegrationService.remove(
                new LambdaQueryWrapper<TbIntegration>()
                        .eq(TbIntegration::getName, name));
    }
}
