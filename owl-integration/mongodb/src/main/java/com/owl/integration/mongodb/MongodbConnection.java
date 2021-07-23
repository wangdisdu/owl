package com.owl.integration.mongodb;

import com.owl.api.IntegrationConnection;
import com.owl.api.schema.IntegrationSchema;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MongodbConnection implements IntegrationConnection {
    private Connection connection;
    private IntegrationSchema schema;
    private MongodbIntegration integration;

    @Override
    public Connection getConnection() {
        return connection;
    }

    public MongodbConnection setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    @Override
    public IntegrationSchema getSchema() {
        return schema;
    }

    public MongodbConnection setSchema(IntegrationSchema schema) {
        this.schema = schema;
        return this;
    }

    @Override
    public MongodbIntegration getIntegration() {
        return integration;
    }

    public MongodbConnection setIntegration(MongodbIntegration integration) {
        this.integration = integration;
        return this;
    }

    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
        if (integration != null) {
            integration.close();
        }
    }
}
