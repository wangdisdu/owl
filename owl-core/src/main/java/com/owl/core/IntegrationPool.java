package com.owl.core;

import com.owl.api.IntegrationConnection;
import com.owl.api.IntegrationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class IntegrationPool {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationPool.class);

    public static final ConcurrentHashMap<String, IntegrationConnection> POOL = new ConcurrentHashMap<>();

    public static IntegrationConnection connect(
            IntegrationContext context,
            BuilderConfig config) throws Exception {
        return POOL.compute(context.getName(), (k, v) -> {
            if (v == null) {
                return create(context, config);
            }
            String lastId = v.getIntegration().getContext().getConnectionId();
            String currId = context.getConnectionId();
            if (currId.equals(lastId)) {
                return v;
            }
            try {
                v.close();
            } catch (Exception e) {
                throw new ConnectionException("refresh connection failed", e);
            }
            return create(context, config);
        });
    }

    private static IntegrationConnection create(
            IntegrationContext context,
            BuilderConfig config) throws ConnectionException {
        try {
            logger.info("create connection to {}", context.getName());
            IntegrationConnection conn = IntegrationCore.connect(context, config);
            logger.info("complete connection to {}", context.getName());
            return conn;
        } catch (Exception e) {
            throw new ConnectionException("create connection failed", e);
        }
    }
}
