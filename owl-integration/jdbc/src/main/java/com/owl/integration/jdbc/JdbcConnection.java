package com.owl.integration.jdbc;

import com.owl.api.IntegrationConnection;
import com.owl.api.schema.IntegrationSchema;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcConnection implements IntegrationConnection {
    private Connection connection;
    private IntegrationSchema schema;
    private JdbcIntegration integration;

    @Override
    public Connection getConnection() {
        return connection;
    }

    public JdbcConnection setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    @Override
    public IntegrationSchema getSchema() {
        return schema;
    }

    public JdbcConnection setSchema(IntegrationSchema schema) {
        this.schema = schema;
        return this;
    }

    @Override
    public JdbcIntegration getIntegration() {
        return integration;
    }

    public JdbcConnection setIntegration(JdbcIntegration integration) {
        this.integration = integration;
        return this;
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
