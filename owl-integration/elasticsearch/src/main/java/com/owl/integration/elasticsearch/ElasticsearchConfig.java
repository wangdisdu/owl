package com.owl.integration.elasticsearch;

import com.owl.api.SmartConfig;
import com.owl.api.IntegrationConfig;
import com.owl.api.annotation.Parameter;

public class ElasticsearchConfig extends SmartConfig implements IntegrationConfig {
    @Parameter
    private String hosts;
    @Parameter
    private String index;
    @Parameter(required = false)
    private String username;
    @Parameter(required = false)
    private String password;

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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
}
