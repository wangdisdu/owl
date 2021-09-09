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

public class ConsumerStats {
    @Category
    private String category = "consumer";
    @Time
    private long timestamp;
    @Instance
    @Tag
    private String group;
    @Instance
    @Tag
    private String topic;
    @Value(alias = "消费量", unit = "count")
    private Long offset;
    @Value(alias = "延时量", unit = "count")
    private Long lag;
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

    public ConsumerStats setCategory(String category) {
        this.category = category;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ConsumerStats setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public ConsumerStats setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public ConsumerStats setGroup(String group) {
        this.group = group;
        return this;
    }

    public Long getOffset() {
        return offset;
    }

    public ConsumerStats setOffset(Long offset) {
        this.offset = offset;
        return this;
    }

    public Long getLag() {
        return lag;
    }

    public ConsumerStats setLag(Long lag) {
        this.lag = lag;
        return this;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }

    public ConsumerStats setPartitions(List<Partition> partitions) {
        this.partitions = partitions;
        return this;
    }

    public static final class Partition {
        @Category
        private String category = "consumer-partition";
        @Time
        private long timestamp;
        @Instance
        @Tag
        private String group;
        @Instance
        @Tag
        private String topic;
        @Instance
        @Tag
        private Integer partition;
        @Value(alias = "消费量", unit = "count")
        private Long offset;
        @Value(alias = "延时量", unit = "count")
        private Long lag;

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

        public String getGroup() {
            return group;
        }

        public Partition setGroup(String group) {
            this.group = group;
            return this;
        }

        public Long getOffset() {
            return offset;
        }

        public Partition setOffset(Long offset) {
            this.offset = offset;
            return this;
        }

        public Long getLag() {
            return lag;
        }

        public Partition setLag(Long lag) {
            this.lag = lag;
            return this;
        }
    }
}
