package com.owl.integration.db2;

import com.owl.api.annotation.Integration;
import com.owl.integration.jdbc.BaseIntegration;

import java.util.Map;

@Integration(
        display = "Db2",
        description = "Db2 Integration",
        sqlPlaceholder = "SELECT * FROM",
        icon = "Db2.svg"
)
public class Db2Integration extends BaseIntegration<Db2Config> {

    @Override
    public Db2Config configure(Map<String, Object> map) {
        Db2Config config = new Db2Config();
        config.configure(map);
        return config;
    }

}
