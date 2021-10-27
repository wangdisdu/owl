package com.owl.integration.kafka;

import com.owl.api.MetricStats;
import com.owl.api.monitor.Metric;
import com.owl.integration.kafka.monitor.ConsumerStats;
import com.owl.integration.kafka.monitor.KafkaMonitor;
import com.owl.integration.kafka.monitor.ProducerStats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KafkaStats implements MetricStats {
    private final KafkaMonitor monitor;
    private Metric[] last = new Metric[0];

    public KafkaStats(KafkaConfig config) {
        this.monitor = new KafkaMonitor(config);
    }

    @Override
    public Metric[] last() {
        return last;
    }

    @Override
    public Metric[] stats() throws Exception {
        List<Metric> list = new ArrayList<>();

        List<ProducerStats> producerStats = monitor.statsProducers();
        List<ConsumerStats> consumerStats = monitor.statsConsumers();

        setLag(producerStats, consumerStats);

        for (ConsumerStats stats : consumerStats) {
            Metric[] metrics = stats.stats();
            for (Metric metric : metrics) {
                if (ConsumerStats.CONSUMER.equals(metric.getCategory())) {
                    list.add(metric);
                }
            }
        }
        for (ProducerStats stats : producerStats) {
            Metric[] metrics = stats.stats();
            for (Metric metric : metrics) {
                if (ProducerStats.PRODUCER.equals(metric.getCategory())) {
                    list.add(metric);
                }
            }
        }
        last = list.toArray(new Metric[0]);
        return last;
    }

    private void setLag(List<ProducerStats> producerStats, List<ConsumerStats> consumerStats) {
        Map<String, Map<Integer, ProducerStats.Partition>> producerMap = producerStats.stream().collect(
                Collectors.toMap(ProducerStats::getTopic,
                        i -> i.getPartitions().stream().collect(
                                Collectors.toMap(ProducerStats.Partition::getPartition, j -> j)
                        )
                )
        );
        for (ConsumerStats consumer : consumerStats) {
            Map<Integer, ProducerStats.Partition> producer = producerMap.get(consumer.getTopic());
            Long lagSum = 0L;
            boolean hasLag = false;
            if (producer != null) {
                for (ConsumerStats.Partition consumerPartition : consumer.getPartitions()) {
                    ProducerStats.Partition producerPartition = producer.get(consumerPartition.getPartition());
                    if (producerPartition != null) {
                        Long lag = producerPartition.getEndOffset() - consumerPartition.getOffset();
                        consumerPartition.setLag(lag);
                        lagSum = lagSum + lag;
                        hasLag = true;
                    } else {
                        consumerPartition.setLag(-1L);
                    }
                }
            }
            if (hasLag) {
                consumer.setLag(lagSum);
            } else {
                consumer.setLag(-1L);
            }
        }
    }

    @Override
    public void close() throws IOException {
        monitor.close();
    }
}
