package com.owl.web.provider;

import com.owl.api.IntegrationConnection;
import com.owl.api.monitor.Metric;

public class MetricCollectHandler implements ConnectionHandler {
    private Metric[] data = new Metric[0];

    public Metric[] getData() {
        return data;
    }

    @Override
    public void handle(IntegrationConnection connection) throws Exception {
        if (connection.getMetricStats() == null) {
            data = new Metric[0];
        } else {
            data = connection.getMetricStats().stats();
        }
    }
}
