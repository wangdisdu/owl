package com.owl.integration.mysql;

import com.owl.api.annotation.Parameter;
import com.owl.integration.jdbc.JdbcConfig;

public class MysqlConfig extends JdbcConfig {
    @Parameter(
            display = "连接",
            placeholder = "jdbc:mysql://localhost:3306/db?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
    )
    private String jdbcUrl;

    @Parameter(
            display = "驱动",
            placeholder = "com.mysql.cj.jdbc.Driver",
            required = false
    )
    private String jdbcDriver;

    @Override
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public MysqlConfig setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    @Override
    public String getJdbcDriver() {
        return jdbcDriver;
    }

    @Override
    public MysqlConfig setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
        return this;
    }
}
