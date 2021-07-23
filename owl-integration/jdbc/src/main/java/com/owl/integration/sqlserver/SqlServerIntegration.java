package com.owl.integration.sqlserver;

import com.owl.api.annotation.Integration;
import com.owl.integration.jdbc.BaseIntegration;

import java.util.Map;

@Integration(
        display = "SqlServer",
        description = "SqlServer Integration",
        sqlPlaceholder = "SELECT * FROM",
        icon = "SqlServer.svg"
)
public class SqlServerIntegration extends BaseIntegration<SqlServerConfig> {

    @Override
    public SqlServerConfig configure(Map<String, Object> map) {
        SqlServerConfig config = new SqlServerConfig();
        config.configure(map);
        return config;
    }

}
