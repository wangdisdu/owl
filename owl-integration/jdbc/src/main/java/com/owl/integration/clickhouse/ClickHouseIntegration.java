package com.owl.integration.clickhouse;

import com.owl.api.annotation.Integration;
import com.owl.integration.jdbc.BaseIntegration;

import java.util.Map;

@Integration(
        display = "ClickHouse",
        description = "ClickHouse Integration",
        sqlPlaceholder = "SELECT * FROM",
        icon = "ClickHouse.svg"
)
public class ClickHouseIntegration extends BaseIntegration<ClickHouseConfig> {

    @Override
    public ClickHouseConfig configure(Map<String, Object> map) {
        ClickHouseConfig config = new ClickHouseConfig();
        config.configure(map);
        return config;
    }

}
