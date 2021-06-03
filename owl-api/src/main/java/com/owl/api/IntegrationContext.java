package com.owl.api;

import java.util.HashMap;
import java.util.Map;

public class IntegrationContext {
    private Map<String, Object> environment = new HashMap<>();

    public void put(String name, Object value) {
        environment.put(name, value);
    }

    public Object get(String name) {
        return environment.get(name);
    }

    public String getString(String name) {
        return (String) environment.get(name);
    }

    public Long getLong(String name) {
        return (Long) environment.get(name);
    }

    public Integer getInteger(String name) {
        return (Integer) environment.get(name);
    }

    public Double getDouble(String name) {
        return (Double) environment.get(name);
    }

    public Float getFloat(String name) {
        return (Float) environment.get(name);
    }
}
