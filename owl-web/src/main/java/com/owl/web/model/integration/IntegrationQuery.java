package com.owl.web.model.integration;

public class IntegrationQuery {
    private String name;
    private String sql;

    public String getName() {
        return name;
    }

    public IntegrationQuery setName(String name) {
        this.name = name;
        return this;
    }

    public String getSql() {
        return sql;
    }

    public IntegrationQuery setSql(String sql) {
        this.sql = sql;
        return this;
    }
}
