package com.owl.web.provider;

import com.owl.api.IntegrationConnection;
import com.owl.api.monitor.Data;

public class MetricCollectHandler implements ConnectionHandler {
    private Data[] data;

    public Data[] getData() {
        return data;
    }

    @Override
    public void handle(IntegrationConnection connection) throws Exception {
        if (connection.getMetricStats() == null) {
            data = new Data[0];
        } else {
            data = connection.getMetricStats().stats();
        }
    }
}
