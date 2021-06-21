package com.owl.integration.elasticsearch;

import com.owl.api.IntegrationConnection;
import com.owl.api.schema.IntegrationSchema;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ElasticsearchConnection implements IntegrationConnection {
    private Connection connection;
    private IntegrationSchema schema;
    private ElasticsearchIntegration integration;

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
    public ElasticsearchIntegration getIntegration() {
        return integration;
    }

    public void setIntegration(ElasticsearchIntegration integration) {
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
