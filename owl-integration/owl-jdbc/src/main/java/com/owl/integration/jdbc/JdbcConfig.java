package com.owl.integration.jdbc;

import com.owl.api.IntegrationConfig;
import com.owl.api.SmartConfig;
import com.owl.api.annotation.KeyValue;
import com.owl.api.annotation.Parameter;

public class JdbcConfig extends SmartConfig implements IntegrationConfig {
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

    @Parameter(
            display = "账号",
            required = false
    )
    private String jdbcUser;

    @Parameter(
            display = "密码",
            required = false,
            password = true
    )
    private String jdbcPassword;

    @Parameter(
            display = "Catalog",
            required = false
    )
    private String jdbcCatalog;

    @Parameter(
            display = "Schema",
            required = false
    )
    private String jdbcSchema;

    @Parameter(
            display = "方言",
            required = false
    )
    private String lexical = "MYSQL";

    @Parameter(
            display = "高级配置",
            required = false
    )
    private KeyValue jdbcProperties;

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public JdbcConfig setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
        return this;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public JdbcConfig setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    public String getJdbcUser() {
        return jdbcUser;
    }

    public JdbcConfig setJdbcUser(String jdbcUser) {
        this.jdbcUser = jdbcUser;
        return this;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public JdbcConfig setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
        return this;
    }

    public String getJdbcCatalog() {
        return jdbcCatalog;
    }

    public JdbcConfig setJdbcCatalog(String jdbcCatalog) {
        this.jdbcCatalog = jdbcCatalog;
        return this;
    }

    public String getJdbcSchema() {
        return jdbcSchema;
    }

    public JdbcConfig setJdbcSchema(String jdbcSchema) {
        this.jdbcSchema = jdbcSchema;
        return this;
    }

    public String getLexical() {
        return lexical;
    }

    public JdbcConfig setLexical(String lexical) {
        this.lexical = lexical;
        return this;
    }

    public KeyValue getJdbcProperties() {
        return jdbcProperties;
    }

    public JdbcConfig setJdbcProperties(KeyValue jdbcProperties) {
        this.jdbcProperties = jdbcProperties;
        return this;
    }
}
