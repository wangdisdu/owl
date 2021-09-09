package com.owl.web.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.owl.api.monitor.Metric;
import com.owl.web.dao.entity.TbHistory;
import com.owl.web.dao.entity.TbIntegration;
import com.owl.web.dao.service.TbHistoryService;
import com.owl.web.dao.service.TbIntegrationService;
import com.owl.web.model.monitor.History;
import com.owl.web.provider.IntegrationPoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableAsync
@EnableScheduling
public class OwlSchedule {
    private static final Logger logger = LoggerFactory.getLogger(OwlSchedule.class);
    private static final long retention = 7 * 24 * 3600 * 1000L; // 7days

    @Autowired
    private TbIntegrationService tbIntegrationService;
    @Autowired
    private TbHistoryService tbHistoryService;
    @Autowired
    private IntegrationPoolService integrationPoolService;

    @Async("OwlMonitorMetricCollectPool")
    @Scheduled(cron = "0 * * * * ?")
    public void integrationMetric() {
        List<TbIntegration> list = tbIntegrationService.list();
        for (TbIntegration integration : list) {
            logger.info("collect metric {}", integration.getName());
            try {
                Metric[] data = integrationPoolService.metric(integration);
                List<TbHistory> batch = new ArrayList<>();
                for (Metric item : data) {
                    TbHistory history = new History()
                            .copyFrom(item)
                            .copyTo(new TbHistory())
                            .setIntegrationName(integration.getName());
                    batch.add(history);
                }
                if (batch.size() > 0) {
                    tbHistoryService.saveBatch(batch);
                }
            } catch (Exception e) {
                logger.error("collect metric error {}", integration.getName(), e);
            }
            logger.info("finish metric {}", integration.getName());
        }
        // 仅保留近7天数据
        tbHistoryService.remove(new LambdaQueryWrapper<TbHistory>()
                .lt(TbHistory::getTime, System.currentTimeMillis() - retention)
        );
    }

}
