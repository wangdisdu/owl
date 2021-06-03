package com.owl.api;

import com.owl.api.reflect.ParameterConfigurer;

import java.util.Map;

public abstract class SmartConfig implements IntegrationConfig {
    private Map<String, Object> parameters;

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void configure(Map<String, Object> parameters) {
        this.parameters = parameters;
        ParameterConfigurer.config(this, parameters);
    }
}
