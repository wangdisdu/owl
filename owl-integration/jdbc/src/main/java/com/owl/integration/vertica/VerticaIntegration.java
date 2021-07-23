package com.owl.integration.vertica;

import com.owl.api.annotation.Integration;
import com.owl.integration.jdbc.BaseIntegration;

import java.util.Map;

@Integration(
        display = "Vertica",
        description = "Vertica Integration",
        sqlPlaceholder = "SELECT * FROM",
        icon = "Vertica.svg"
)
public class VerticaIntegration extends BaseIntegration<VerticaConfig> {

    @Override
    public VerticaConfig configure(Map<String, Object> map) {
        VerticaConfig config = new VerticaConfig();
        config.configure(map);
        return config;
    }

}
