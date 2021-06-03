package com.owl.api;

import java.sql.Connection;
import java.util.Map;

public interface IntegrationBuilder<T> {

    void build(IntegrationContext context, T config) throws Exception;

    T configure(Map<String, Object> map);

    Connection connect() throws Exception;
}