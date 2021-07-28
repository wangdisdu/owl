package com.owl.integration.elasticsearch;

import com.owl.api.IntegrationConfig;
import com.owl.api.SmartConfig;
import com.owl.api.annotation.Parameter;

public class ElasticsearchConfig extends SmartConfig implements IntegrationConfig {
    @Parameter(
            display = "地址",
            placeholder = "host1:9200,host2:9200"
    )
    private String hosts;

    @Parameter(
            display = "账号",
            required = false
    )
    private String username;

    @Parameter(
            display = "密码",
            required = false,
            password = true
    )
    private String password;

    @Parameter(
            display = "URL前缀",
            required = false
    )
    private String pathPrefix;

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }
}
