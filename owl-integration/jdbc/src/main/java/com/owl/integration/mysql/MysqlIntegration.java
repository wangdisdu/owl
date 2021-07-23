package com.owl.integration.mysql;

import com.owl.api.annotation.Integration;
import com.owl.integration.jdbc.BaseIntegration;

import java.util.Map;

@Integration(
        display = "Mysql",
        description = "Mysql Integration",
        sqlPlaceholder = "SELECT * FROM",
        icon = "Mysql.svg"
)
public class MysqlIntegration extends BaseIntegration<MysqlConfig> {

    @Override
    public MysqlConfig configure(Map<String, Object> map) {
        MysqlConfig config = new MysqlConfig();
        config.configure(map);
        return config;
    }

}
