package com.owl.web.provider;

import com.owl.api.IntegrationConnection;

public interface ConnectionHandler {
    void handle(IntegrationConnection connection) throws Exception;
}
