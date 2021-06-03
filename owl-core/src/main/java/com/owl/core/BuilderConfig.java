package com.owl.core;

import java.util.Map;

public class BuilderConfig {
    private String builder;
    private Map<String, Object> config;

    public String getBuilder() {
        return builder;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
}
