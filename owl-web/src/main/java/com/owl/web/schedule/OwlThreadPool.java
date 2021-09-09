package com.owl.web.schedule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class OwlThreadPool {

    @Bean("OwlMonitorMetricCollectPool")
    public AsyncTaskExecutor owlMonitorMetricCollectPool() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("OwlMonitorMetricCollect");
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(10);
        return taskExecutor;
    }

}
