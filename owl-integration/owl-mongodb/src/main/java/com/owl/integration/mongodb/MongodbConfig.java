package com.owl.integration.mongodb;

import com.owl.api.IntegrationConfig;
import com.owl.api.SmartConfig;
import com.owl.api.annotation.Parameter;

public class MongodbConfig extends SmartConfig implements IntegrationConfig {
    @Parameter(
            display = "地址",
            placeholder = "127.0.0.1"
    )
    private String host;

    @Parameter(
            display = "端口",
            placeholder = "27017"
    )
    private Integer port;

    @Parameter(
            display = "数据库"
    )
    private String database;

    @Parameter(
            display = "认证方式",
            required = false
    )
    private String authMechanism;

    @Parameter(
            display = "用户名",
            required = false
    )
    private String username;

    @Parameter(
            display = "认证db",
            required = false
    )
    private String authDatabase;

    @Parameter(
            display = "密码",
            required = false
    )
    private String password;

    public String getHost() {
        return host;
    }

    public MongodbConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public MongodbConfig setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getDatabase() {
        return database;
    }

    public MongodbConfig setDatabase(String database) {
        this.database = database;
        return this;
    }

    public String getAuthMechanism() {
        return authMechanism;
    }

    public MongodbConfig setAuthMechanism(String authMechanism) {
        this.authMechanism = authMechanism;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public MongodbConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getAuthDatabase() {
        return authDatabase;
    }

    public MongodbConfig setAuthDatabase(String authDatabase) {
        this.authDatabase = authDatabase;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MongodbConfig setPassword(String password) {
        this.password = password;
        return this;
    }
}
