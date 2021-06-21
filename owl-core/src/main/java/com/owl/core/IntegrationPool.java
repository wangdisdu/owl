package com.owl.core;

import com.owl.api.IntegrationConnection;
import com.owl.api.IntegrationContext;

import java.util.HashMap;
import java.util.Map;

public class IntegrationPool {
    public static final Map<String, IntegrationConnection> POOL = new HashMap<>();

    public static IntegrationConnection connect(
            IntegrationContext context,
            BuilderConfig config) throws Exception {
        IntegrationConnection cache = POOL.get(context.getName());
        if (cache == null) {
            return create(context, config);
        }
        String lastId = cache.getIntegration().getContext().getConnectionId();
        String currId = context.getConnectionId();
        if(currId.equals(lastId)) {
            return cache;
        }
        cache.close();
        return create(context, config);
    }

    private static IntegrationConnection create(
            IntegrationContext context,
            BuilderConfig config) throws Exception {
        IntegrationConnection connection = IntegrationCore.connect(context, config);
        POOL.put(context.getName(), connection);
        return connection;
    }
}
