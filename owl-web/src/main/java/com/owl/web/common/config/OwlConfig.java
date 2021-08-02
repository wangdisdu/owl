package com.owl.web.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "owl")
public class OwlConfig {
    private String home;

    public String getHome() {
        return home;
    }

    public OwlConfig setHome(String home) {
        this.home = home;
        return this;
    }
}
