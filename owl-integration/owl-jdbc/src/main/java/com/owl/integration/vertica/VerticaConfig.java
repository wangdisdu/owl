package com.owl.integration.vertica;

import com.owl.api.annotation.Parameter;
import com.owl.integration.jdbc.JdbcConfig;

public class VerticaConfig extends JdbcConfig {
    @Parameter(
            display = "连接",
            placeholder = "jdbc:vertica://localhost:5433/db?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
    )
    private String jdbcUrl;

    @Parameter(
            display = "驱动",
            placeholder = "com.vertica.jdbc.Driver",
            required = false
    )
    private String jdbcDriver;

    @Parameter(
            display = "Schema",
            required = true
    )
    private String jdbcSchema;

    @Override
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public VerticaConfig setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    @Override
    public String getJdbcDriver() {
        return jdbcDriver;
    }

    @Override
    public VerticaConfig setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
        return this;
    }

    @Override
    public String getJdbcSchema() {
        return jdbcSchema;
    }

    @Override
    public VerticaConfig setJdbcSchema(String jdbcSchema) {
        this.jdbcSchema = jdbcSchema;
        return this;
    }
}
