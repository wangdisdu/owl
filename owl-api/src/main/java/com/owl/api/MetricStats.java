package com.owl.api;

import com.owl.api.monitor.Data;

import java.io.Closeable;

public interface MetricStats extends Closeable {
    Data[] stats();
}
