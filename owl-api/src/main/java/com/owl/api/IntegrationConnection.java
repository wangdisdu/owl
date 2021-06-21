package com.owl.api;

import com.owl.api.schema.IntegrationSchema;

import java.io.Closeable;
import java.sql.Connection;

public interface IntegrationConnection extends Closeable {
    Connection getConnection();
    IntegrationSchema getSchema();
    IntegrationBuilder getIntegration();
}
