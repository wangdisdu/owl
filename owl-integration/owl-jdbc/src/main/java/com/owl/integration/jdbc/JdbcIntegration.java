package com.owl.integration.jdbc;

import com.owl.api.annotation.Integration;

import java.util.Map;

@Integration(
        display = "Jdbc",
        description = "Jdbc Integration",
        sqlPlaceholder = "SELECT * FROM",
        icon = "Jdbc.svg"
)
public class JdbcIntegration extends BaseIntegration<JdbcConfig> {

    @Override
    public JdbcConfig configure(Map<String, Object> map) {
        JdbcConfig config = new JdbcConfig();
        config.configure(map);
        return config;
    }

}
