package com.owl.integration.db2;

import com.owl.api.annotation.Parameter;
import com.owl.integration.jdbc.JdbcConfig;

public class Db2Config extends JdbcConfig {
    @Parameter(
            display = "连接",
            placeholder = "jdbc:db2://localhost:50000/db"
    )
    private String jdbcUrl;

    @Parameter(
            display = "驱动",
            placeholder = "com.ibm.db2.jcc.DB2Driver",
            required = false
    )
    private String jdbcDriver;

    @Override
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public Db2Config setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    @Override
    public String getJdbcDriver() {
        return jdbcDriver;
    }

    @Override
    public Db2Config setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
        return this;
    }
}
