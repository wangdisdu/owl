package com.owl.api;

import java.io.Closeable;
import java.util.Map;

public interface IntegrationBuilder<T> extends Closeable {

    void build(IntegrationContext context, T config) throws Exception;

    T configure(Map<String, Object> map);

    IntegrationContext getContext();

    IntegrationConnection connect() throws Exception;
}