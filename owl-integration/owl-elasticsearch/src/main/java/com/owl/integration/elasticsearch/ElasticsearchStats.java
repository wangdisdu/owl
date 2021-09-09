package com.owl.integration.elasticsearch;

import com.owl.api.MetricStats;
import com.owl.api.monitor.Metric;
import com.owl.api.monitor.MetricUtil;
import com.owl.integration.elasticsearch.monitor.ClusterStats;
import com.owl.integration.elasticsearch.monitor.ElasticsearchMonitor;
import com.owl.integration.elasticsearch.monitor.NodeStats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticsearchStats implements MetricStats {
    private final ElasticsearchConfig config;
    private final ElasticsearchMonitor monitor;
    private Metric[] last = new Metric[0];

    public ElasticsearchStats(ElasticsearchConfig config) throws IOException {
        this.config = config;
        this.monitor = new ElasticsearchMonitor(config);
    }

    @Override
    public Metric[] last() {
        return last;
    }

    @Override
    public Metric[] stats() {
        List<Metric> result = new ArrayList<>();
        ClusterStats clusterStats = monitor.clusterStats();
        NodeStats[] nodeStats = monitor.nodeStats();
        String nodes = Arrays.stream(nodeStats)
                .map(NodeStats::getHost)
                .collect(Collectors.joining(","));
        Metric[] clusterData = MetricUtil.read(clusterStats);
        for (Metric metric : clusterData) {
            metric.setHost(nodes);
            metric.setGuid(MetricUtil.genGuid(
                    metric.getMetric(),
                    clusterStats.getName()
            ));
            metric.setTag1(clusterStats.getName());
            result.add(metric);
        }
        for (NodeStats node : nodeStats) {
            Metric[] nodeData = MetricUtil.read(node);
            for (Metric metric : nodeData) {
                metric.setGuid(MetricUtil.genGuid(
                        metric.getMetric(),
                        clusterStats.getName(),
                        node.getName()
                ));
                metric.setTag1(clusterStats.getName());
                metric.setTag2(node.getName());
                result.add(metric);
            }
        }
        last = result.toArray(new Metric[0]);
        return last;
    }

    @Override
    public void close() throws IOException {
        monitor.close();
    }
}
