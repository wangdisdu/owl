package com.owl.integration.elasticsearch;

import com.owl.api.MetricStats;
import com.owl.api.monitor.Data;
import com.owl.api.monitor.DataUtil;
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

    public ElasticsearchStats(ElasticsearchConfig config) throws IOException {
        this.config = config;
        this.monitor = new ElasticsearchMonitor(config);
    }

    @Override
    public Data[] stats() {
        List<Data> result = new ArrayList<>();
        ClusterStats clusterStats = monitor.clusterStats();
        NodeStats[] nodeStats = monitor.nodeStats();
        String nodes = Arrays.stream(nodeStats)
                .map(NodeStats::getHost)
                .collect(Collectors.joining(","));
        Data[] clusterData = DataUtil.read(clusterStats);
        for (Data data : clusterData) {
            data.setHost(nodes);
            data.setGuid(DataUtil.genGuid(
                    data.getMetric(),
                    clusterStats.getName()
            ));
            data.setTag1(clusterStats.getName());
            result.add(data);
        }
        for (NodeStats node : nodeStats) {
            Data[] nodeData = DataUtil.read(node);
            for (Data data : nodeData) {
                data.setGuid(DataUtil.genGuid(
                        data.getMetric(),
                        clusterStats.getName(),
                        node.getName()
                ));
                data.setTag1(clusterStats.getName());
                data.setTag2(node.getName());
                result.add(data);
            }
        }
        return result.toArray(new Data[0]);
    }

    @Override
    public void close() throws IOException {
        if (monitor != null) {
            monitor.close();
        }
    }
}
