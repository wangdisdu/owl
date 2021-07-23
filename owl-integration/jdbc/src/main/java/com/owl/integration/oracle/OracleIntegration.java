package com.owl.integration.oracle;

import com.owl.api.annotation.Integration;
import com.owl.integration.jdbc.BaseIntegration;

import java.util.Map;

@Integration(
        display = "Oracle",
        description = "Oracle Integration",
        sqlPlaceholder = "SELECT * FROM",
        icon = "Oracle.svg"
)
public class OracleIntegration extends BaseIntegration<OracleConfig> {

    @Override
    public OracleConfig configure(Map<String, Object> map) {
        OracleConfig config = new OracleConfig();
        config.configure(map);
        return config;
    }

}
