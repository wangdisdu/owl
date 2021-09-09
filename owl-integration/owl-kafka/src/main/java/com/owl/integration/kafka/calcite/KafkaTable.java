package com.owl.integration.kafka.calcite;

import cn.hutool.core.collection.CollUtil;
import com.owl.api.schema.TableColumn;
import com.owl.api.schema.TableSchema;
import com.owl.integration.kafka.KafkaConfig;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.TranslatableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Types;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class KafkaTable extends AbstractTable implements TranslatableTable, Closeable {
    private final String topic;
    private final KafkaConfig config;
    private final TableSchema tableSchema;

    public KafkaTable(String topic, KafkaConfig config) {
        this.topic = topic;
        this.config = config;
        this.tableSchema = createTableSchema(topic);
    }

    public TableSchema getTableSchema() {
        return tableSchema;
    }

    private TableSchema createTableSchema(String topic) {
        final TableSchema tableSchema = new TableSchema();
        tableSchema.setName(topic);
        tableSchema.addColumn(TableColumn.build(Const.PARTITION, Types.INTEGER));
        tableSchema.addColumn(TableColumn.build(Const.TIMESTAMP, Types.BIGINT));
        tableSchema.addColumn(TableColumn.build(Const.OFFSET, Types.BIGINT));
        tableSchema.addColumn(TableColumn.build(Const.KEY, Types.VARCHAR));
        tableSchema.addColumn(TableColumn.build(Const.VALUE, Types.VARCHAR));
        return tableSchema;
    }

    @Override
    public RelNode toRel(RelOptTable.ToRelContext context, RelOptTable relOptTable) {
        final RelOptCluster cluster = context.getCluster();
        return new KafkaTableScan(cluster, relOptTable, this);
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        final RelDataTypeFactory.Builder builder = typeFactory.builder();
        for (TableColumn column : tableSchema.getColumns()) {
            SqlTypeName sqlTypeName = SqlTypeName.getNameForJdbcType(column.getJdbcType().code);
            RelDataType relDataType = typeFactory.createSqlType(sqlTypeName);
            builder.add(column.getName(), relDataType).nullable(true);
        }
        return builder.build();
    }

    @Override
    public String toString() {
        return "KafkaTable{" + topic + "}";
    }

    @Override
    public void close() throws IOException {
    }

    private KafkaConsumer<String, String> createConsumer(KafkaRelNode.Implementor implementor) {
        Properties props = new Properties();
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "OWL-Client");
        Optional.ofNullable(config.getConsumerProperties()).ifPresent(i -> i.forEach(props::put));
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        if (implementor.getLimit() != null) {
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, implementor.getLimit().intValue());
        }
        return new KafkaConsumer<>(props);
    }

    public List<Object> poll(KafkaRelNode.Implementor implementor) {
        try (KafkaConsumer<String, String> consumer = createConsumer(implementor)) {
            List<TopicPartition> partitions = partitions(consumer, implementor);
            if (partitions.isEmpty()) {
                return new ArrayList<>();
            }
            if (CollUtil.isNotEmpty(implementor.getAgg())) {
                return aggResults(consumer, implementor, partitions);
            }
            return pollResults(consumer, implementor, partitions);
        }
    }

    private List<TopicPartition> partitions(KafkaConsumer<String, String> consumer, KafkaRelNode.Implementor implementor) {
        List<TopicPartition> partitions = new ArrayList<>();
        List<PartitionInfo> infos = consumer.partitionsFor(topic);
        for (PartitionInfo info : infos) {
            if (CollUtil.isEmpty(implementor.getPartitions())
                    || CollUtil.contains(implementor.getPartitions(), info.partition())) {
                TopicPartition partition = new TopicPartition(info.topic(), info.partition());
                partitions.add(partition);
            }
        }
        if (partitions.isEmpty()) {
            return partitions;
        }
        Map<TopicPartition, Long> beginningOffsets = consumer.beginningOffsets(partitions, Duration.ofSeconds(1));
        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions, Duration.ofSeconds(1));
        List<TopicPartition> assigns = new ArrayList<>();
        for (TopicPartition partition : partitions) {
            Long from = implementor.getOffsetFrom();
            Long to = implementor.getOffsetTo();
            Long beginningOffset = beginningOffsets.get(partition);
            Long endOffset = endOffsets.get(partition);
            if ((from == null || from <= endOffset) && (to == null || to >= beginningOffset)) {
                assigns.add(partition);
            }
        }
        return assigns;
    }

    private List<Object> pollResults(KafkaConsumer<String, String> consumer, KafkaRelNode.Implementor implementor, List<TopicPartition> partitions) {
        List<Object> list = new ArrayList<>();
        consumer.assign(partitions);
        for (TopicPartition partition : partitions) {
            if (implementor.getOffsetFrom() != null) {
                consumer.seek(partition, implementor.getOffsetFrom());
            } else {
                consumer.seekToBeginning(Collections.singletonList(partition));
            }
        }

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(2));
        for (ConsumerRecord record : records) {
            if (implementor.getOffsetTo() != null && record.offset() > implementor.getOffsetTo()) {
                break;
            }
            Object[] row = new Object[5];
            row[0] = record.partition();
            row[1] = record.timestamp();
            row[2] = record.offset();
            row[3] = record.key();
            row[4] = record.value();
            list.add(row);
            if (implementor.getLimit() != null && list.size() >= implementor.getLimit()) {
                break;
            }
        }
        return list;
    }

    private List<Object> aggResults(KafkaConsumer<String, String> consumer, KafkaRelNode.Implementor implementor, List<TopicPartition> partitions) {
        List<Object> list = new ArrayList<>();
        Map<TopicPartition, Long> beginningOffsets = consumer.beginningOffsets(partitions);
        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);
        if (CollUtil.isNotEmpty(implementor.getAgg()) && implementor.getAgg().size() == 1) {
            KafkaRelNode.KafkaAggCall aggCall = implementor.getAgg().get(0);
            list.add(aggCall(aggCall, partitions, beginningOffsets, endOffsets));
        } else if (CollUtil.isNotEmpty(implementor.getAgg())) {
            Object[] row = new Object[implementor.getAgg().size()];
            for (int i = 0; i < implementor.getAgg().size(); i++) {
                KafkaRelNode.KafkaAggCall aggCall = implementor.getAgg().get(i);
                row[i] = aggCall(aggCall, partitions, beginningOffsets, endOffsets);
            }
            list.add(row);
        }
        return list;
    }

    private Object aggCall(KafkaRelNode.KafkaAggCall aggCall,
                           List<TopicPartition> partitions,
                           Map<TopicPartition, Long> beginningOffsets,
                           Map<TopicPartition, Long> endOffsets) {
        if (KafkaRelNode.KafkaAggCall.CountPartition == aggCall) {
            return partitions.size();
        } else if (KafkaRelNode.KafkaAggCall.MaxOffset == aggCall) {
            return endOffsets.values().stream().max(Long::compareTo).orElse(null);
        } else if (KafkaRelNode.KafkaAggCall.MinOffset == aggCall) {
            return beginningOffsets.values().stream().min(Long::compareTo).orElse(null);
        }
        return null;
    }
}
