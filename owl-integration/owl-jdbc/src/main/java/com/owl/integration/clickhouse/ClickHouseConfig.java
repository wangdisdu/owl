package com.owl.integration.clickhouse;

import com.owl.api.annotation.Parameter;
import com.owl.integration.jdbc.JdbcConfig;

public class ClickHouseConfig extends JdbcConfig {
    @Parameter(
            display = "连接",
            placeholder = "jdbc:clickhouse://localhost:8123/db?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
    )
    private String jdbcUrl;

    @Parameter(
            display = "驱动",
            placeholder = "ru.yandex.clickhouse.ClickHouseDriver",
            required = false
    )
    private String jdbcDriver;

    @Override
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public ClickHouseConfig setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    @Override
    public String getJdbcDriver() {
        return jdbcDriver;
    }

    @Override
    public ClickHouseConfig setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
        return this;
    }
}
