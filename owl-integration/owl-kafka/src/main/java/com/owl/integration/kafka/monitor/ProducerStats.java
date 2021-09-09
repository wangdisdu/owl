package com.owl.integration.kafka.monitor;

import com.owl.api.monitor.Category;
import com.owl.api.monitor.Instance;
import com.owl.api.monitor.Metric;
import com.owl.api.monitor.MetricUtil;
import com.owl.api.monitor.Tag;
import com.owl.api.monitor.Time;
import com.owl.api.monitor.Value;

import java.util.ArrayList;
import java.util.List;

public class ProducerStats {
    @Category
    private String category = "producer";
    @Time
    private long timestamp;
    @Instance
    @Tag
    private String topic;
    @Value(alias = "数据量", unit = "count")
    private Long recordsCount;
    @Value(alias = "开始位", unit = "count")
    private Long beginOffset;
    @Value(alias = "结束位", unit = "count")
    private Long endOffset;
    @Value(alias = "分区数", unit = "count")
    private Long  partitionsCount;
    @Value(alias = "分区丢失数", unit = "count")
    private Long  partitionsMiss;
    private List<Partition> partitions;

    public Metric[] stats() {
        List<Metric> list = new ArrayList<>();
        Metric[] topicStats = MetricUtil.read(this);
        for (Metric metric : topicStats) {
            metric.setGuid(MetricUtil.genGuid(
                    metric.getMetric(),
                    metric.getInstance()
            ));
            list.add(metric);
        }
        for (Partition partition : partitions) {
            Metric[] partitionStats = partition.stats();
            for (Metric metric : partitionStats) {
                metric.setGuid(MetricUtil.genGuid(
                        metric.getMetric(),
                        metric.getInstance()
                ));
                list.add(metric);
            }
        }
        return list.toArray(new Metric[0]);
    }

    public String getCategory() {
        return category;
    }

    public ProducerStats setCategory(String category) {
        this.category = category;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ProducerStats setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public ProducerStats setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Long getRecordsCount() {
        return recordsCount;
    }

    public ProducerStats setRecordsCount(Long recordsCount) {
        this.recordsCount = recordsCount;
        return this;
    }

    public Long getBeginOffset() {
        return beginOffset;
    }

    public ProducerStats setBeginOffset(Long beginOffset) {
        this.beginOffset = beginOffset;
        return this;
    }

    public Long getEndOffset() {
        return endOffset;
    }

    public ProducerStats setEndOffset(Long endOffset) {
        this.endOffset = endOffset;
        return this;
    }

    public Long getPartitionsCount() {
        return partitionsCount;
    }

    public ProducerStats setPartitionsCount(Long partitionsCount) {
        this.partitionsCount = partitionsCount;
        return this;
    }

    public Long getPartitionsMiss() {
        return partitionsMiss;
    }

    public ProducerStats setPartitionsMiss(Long partitionsMiss) {
        this.partitionsMiss = partitionsMiss;
        return this;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }

    public ProducerStats setPartitions(List<Partition> partitions) {
        this.partitions = partitions;
        return this;
    }

    public static final class Partition {
        @Category
        private String category = "producer-partition";
        @Time
        private long timestamp;
        @Instance
        @Tag
        private String topic;
        @Instance
        @Tag
        private Integer partition;
        @Tag
        private String leader;
        @Value(alias = "数据量", unit = "count")
        private Long recordsCount;
        @Value(alias = "开始位", unit = "count")
        private Long beginOffset;
        @Value(alias = "结束位", unit = "count")
        private Long endOffset;

        public Metric[] stats() {
            return MetricUtil.read(this);
        }

        public String getCategory() {
            return category;
        }

        public Partition setCategory(String category) {
            this.category = category;
            return this;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public Partition setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public String getTopic() {
            return topic;
        }

        public Partition setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public Integer getPartition() {
            return partition;
        }

        public Partition setPartition(Integer partition) {
            this.partition = partition;
            return this;
        }

        public String getLeader() {
            return leader;
        }

        public Partition setLeader(String leader) {
            this.leader = leader;
            return this;
        }

        public Long getRecordsCount() {
            return recordsCount;
        }

        public Partition setRecordsCount(Long recordsCount) {
            this.recordsCount = recordsCount;
            return this;
        }

        public Long getBeginOffset() {
            return beginOffset;
        }

        public Partition setBeginOffset(Long beginOffset) {
            this.beginOffset = beginOffset;
            return this;
        }

        public Long getEndOffset() {
            return endOffset;
        }

        public Partition setEndOffset(Long endOffset) {
            this.endOffset = endOffset;
            return this;
        }
    }
}
