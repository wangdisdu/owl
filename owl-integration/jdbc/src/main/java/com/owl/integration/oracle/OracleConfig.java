package com.owl.integration.oracle;

import com.owl.api.annotation.Parameter;
import com.owl.integration.jdbc.JdbcConfig;

public class OracleConfig extends JdbcConfig {
    @Parameter(
            display = "连接",
            placeholder = "jdbc:oracle:thin:@localhost:1521:db"
    )
    private String jdbcUrl;

    @Parameter(
            display = "驱动",
            placeholder = "oracle.jdbc.driver.OracleDriver",
            required = false
    )
    private String jdbcDriver;

    @Override
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public OracleConfig setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    @Override
    public String getJdbcDriver() {
        return jdbcDriver;
    }

    @Override
    public OracleConfig setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
        return this;
    }
}
