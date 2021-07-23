package com.owl.integration.sqlserver;

import com.owl.api.annotation.Parameter;
import com.owl.integration.jdbc.JdbcConfig;

public class SqlServerConfig extends JdbcConfig {
    @Parameter(
            display = "连接",
            placeholder = "jdbc:sqlserver://192.168.21.21:1433;DatabaseName=db"
    )
    private String jdbcUrl;

    @Parameter(
            display = "驱动",
            placeholder = "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            required = false
    )
    private String jdbcDriver;

    @Override
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public SqlServerConfig setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    @Override
    public String getJdbcDriver() {
        return jdbcDriver;
    }

    @Override
    public SqlServerConfig setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
        return this;
    }
}
