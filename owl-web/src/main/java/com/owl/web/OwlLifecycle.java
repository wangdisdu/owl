package com.owl.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class OwlLifecycle implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(OwlLifecycle.class);
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() throws Exception {
        logger.info("Owl is Ready");
        OwlApplication.println("Owl is Ready");
    }

    @PreDestroy
    public void destroy() throws Exception {
        logger.info("Owl is Destroying");
    }

}
