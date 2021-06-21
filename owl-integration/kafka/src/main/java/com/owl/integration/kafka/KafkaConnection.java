package com.owl.integration.kafka;

import com.owl.api.IntegrationConnection;
import com.owl.api.schema.IntegrationSchema;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class KafkaConnection implements IntegrationConnection {
    private Connection connection;
    private IntegrationSchema schema;
    private KafkaIntegration integration;

    @Override
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public IntegrationSchema getSchema() {
        return schema;
    }

    public void setSchema(IntegrationSchema schema) {
        this.schema = schema;
    }

    @Override
    public KafkaIntegration getIntegration() {
        return integration;
    }

    public void setIntegration(KafkaIntegration integration) {
        this.integration = integration;
    }

    @Override
    public void close() throws IOException {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
        if(integration != null) {
            integration.close();
        }
    }
}
