package com.owl.web.schedule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class OwlThreadPool {

    @Bean("OwlMonitorMetricCollectPool")
    public AsyncTaskExecutor owlMonitorMetricCollectPool() {
        int cores = Runtime.getRuntime().availableProcessors();
        cores = Math.max(cores, 2);
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("OwlMonitorMetricCollect");
        taskExecutor.setCorePoolSize(cores);
        taskExecutor.setMaxPoolSize(cores * 4);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(60);
        return taskExecutor;
    }

}
