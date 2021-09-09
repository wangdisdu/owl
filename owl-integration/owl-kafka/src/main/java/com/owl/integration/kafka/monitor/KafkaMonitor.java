package com.owl.integration.kafka.monitor;

import com.owl.integration.kafka.KafkaConfig;
import com.owl.integration.kafka.client.KafkaClientBuilder;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class KafkaMonitor implements Closeable {
    public static final int TIMEOUT = 2;
    private final AdminClient adminClient;
    private final KafkaConsumer<String, String> kafkaConsumer;

    public KafkaMonitor(KafkaConfig config) {
        this(KafkaClientBuilder.createAdmin(config),
                KafkaClientBuilder.createConsumer(config));
    }

    public KafkaMonitor(AdminClient adminClient, KafkaConsumer<String, String> kafkaConsumer) {
        this.adminClient = adminClient;
        this.kafkaConsumer = kafkaConsumer;
    }

    public List<ConsumerStats> statsConsumers() throws Exception {
        List<ConsumerStats> result = new ArrayList<>();
        ListConsumerGroupsResult groupsResult = adminClient.listConsumerGroups();
        Collection<ConsumerGroupListing> valid = groupsResult.valid().get(TIMEOUT, TimeUnit.SECONDS);
        for (ConsumerGroupListing consumerGroupList : valid) {
            String groupId = consumerGroupList.groupId();
            try {
                List<ConsumerStats> stats = statsConsumer(groupId);
                result.addAll(stats);
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<ProducerStats> statsProducers() throws Exception {
        List<ProducerStats> result = new ArrayList<>();
        DescribeClusterResult clusterResult = adminClient.describeCluster(new DescribeClusterOptions().timeoutMs(TIMEOUT * 1000));
        Collection<Node> nodes = clusterResult.nodes().get(TIMEOUT, TimeUnit.SECONDS);
        HashSet<Node> nodeSet = new HashSet<>(nodes);
        Map<String, List<PartitionInfo>> topics = kafkaConsumer.listTopics(Duration.ofSeconds(TIMEOUT));
        List<PartitionInfo> partitions = new ArrayList<>();
        Iterator<Map.Entry<String, List<PartitionInfo>>> it = topics.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<PartitionInfo>> entry = it.next();
            List<PartitionInfo> partitionInfos = entry.getValue();
            partitions.addAll(partitionInfos);
            if (partitions.size() >= 1024 || !it.hasNext()) {
                result.addAll(statsProducer(nodeSet, partitions));
                partitions = new ArrayList<>();
            }
        }
        return result;
    }

    public List<ConsumerStats> statsConsumer(String groupId) throws Exception {
        ListConsumerGroupOffsetsResult offsetsResult = adminClient.listConsumerGroupOffsets(groupId);
        Map<TopicPartition, OffsetAndMetadata> offsetMap = offsetsResult.partitionsToOffsetAndMetadata().get(TIMEOUT, TimeUnit.SECONDS);
        long now = System.currentTimeMillis();
        Map<String, List<ConsumerStats.Partition>> groupMap = new HashMap<>();
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsetMap.entrySet()) {
            TopicPartition tp = entry.getKey();
            OffsetAndMetadata offset = entry.getValue();
            ConsumerStats.Partition partitionStats = new ConsumerStats.Partition();
            partitionStats.setTimestamp(now);
            partitionStats.setGroup(groupId);
            partitionStats.setTopic(tp.topic());
            partitionStats.setPartition(tp.partition());
            partitionStats.setOffset(offset.offset());
            String groupKey = tp.topic();
            List<ConsumerStats.Partition> group = groupMap.computeIfAbsent(groupKey, i -> new ArrayList<>());
            group.add(partitionStats);
        }
        return groupMap.values().stream().map(i -> {
            String topic = i.get(0).getTopic();
            Long offset = i.stream().map(ConsumerStats.Partition::getOffset).reduce(0L, Long::sum);
            ConsumerStats consumerStats = new ConsumerStats();
            consumerStats.setTimestamp(now);
            consumerStats.setGroup(groupId);
            consumerStats.setTopic(topic);
            consumerStats.setOffset(offset);
            consumerStats.setPartitions(i);
            return consumerStats;
        }).collect(Collectors.toList());
    }

    public List<ProducerStats> statsProducer(Set<Node> nodes, List<PartitionInfo> partitionInfos) throws Exception {
        List<TopicPartition> partitions = new ArrayList<>();
        for (PartitionInfo info : partitionInfos) {
            if (nodes.contains(info.leader())) {
                partitions.add(new TopicPartition(
                        info.topic(),
                        info.partition()));
            }
        }
        Map<TopicPartition, Long> beginningOffsets = kafkaConsumer.beginningOffsets(partitions, Duration.ofSeconds(TIMEOUT));
        Map<TopicPartition, Long> endOffsets = kafkaConsumer.endOffsets(partitions, Duration.ofSeconds(TIMEOUT));
        long now = System.currentTimeMillis();
        Map<String, ProducerStats> results = new LinkedHashMap<>();
        for (PartitionInfo info : partitionInfos) {
            String topicName = info.topic();
            TopicPartition tp = new TopicPartition(topicName, info.partition());
            ProducerStats stats = results.computeIfAbsent(topicName, i -> {
                ProducerStats s = new ProducerStats();
                s.setTimestamp(now);
                s.setTopic(topicName);
                s.setBeginOffset(0L);
                s.setEndOffset(0L);
                s.setRecordsCount(0L);
                s.setPartitionsCount(0L);
                s.setPartitionsMiss(0L);
                s.setPartitions(new ArrayList<>());
                return s;
            });
            boolean found = beginningOffsets.containsKey(tp) && endOffsets.containsKey(tp);
            if (found) {
                Long begin = beginningOffsets.get(tp);
                Long end = endOffsets.get(tp);
                Long range = end - begin;
                stats.setBeginOffset(stats.getBeginOffset() + begin);
                stats.setEndOffset(stats.getEndOffset() + end);
                stats.setRecordsCount(stats.getRecordsCount() + range);
                stats.setPartitionsCount(stats.getPartitionsCount() + 1);
                ProducerStats.Partition partitionStats = new ProducerStats.Partition();
                partitionStats.setTimestamp(now);
                partitionStats.setTopic(topicName);
                partitionStats.setPartition(info.partition());
                partitionStats.setLeader(info.leader().idString());
                partitionStats.setBeginOffset(begin);
                partitionStats.setEndOffset(end);
                partitionStats.setRecordsCount(range);
                stats.getPartitions().add(partitionStats);
            } else {
                stats.setPartitionsMiss(stats.getPartitionsMiss() + 1);
            }
        }
        return new ArrayList<>(results.values());
    }

    @Override
    public void close() throws IOException {
        if (adminClient != null) {
            adminClient.close(30, TimeUnit.SECONDS);
        }
        if (kafkaConsumer != null) {
            kafkaConsumer.close();
        }
    }
}
