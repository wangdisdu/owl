package com.owl.api;

import com.owl.api.monitor.Metric;

import java.io.Closeable;

public interface MetricStats extends Closeable {
    Metric[] last();

    Metric[] stats() throws Exception;
}
