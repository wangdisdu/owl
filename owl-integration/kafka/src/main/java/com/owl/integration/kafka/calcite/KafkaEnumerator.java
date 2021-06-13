package com.owl.integration.kafka.calcite;

import cn.hutool.core.util.IdUtil;
import com.owl.integration.kafka.KafkaConfig;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaEnumerator implements Enumerator<Object[]> {
    private final String topic;
    private final KafkaConfig config;
    private final AtomicBoolean cancelFlag;

    // runtime
    private KafkaConsumer<String, String> consumer;
    private final LinkedList<ConsumerRecord<String, String>> bufferedRecords = new LinkedList<>();
    private ConsumerRecord<String, String> curRecord;

    public KafkaEnumerator(String topic, KafkaConfig config, AtomicBoolean cancelFlag) {
        this.topic = topic;
        this.config = config;
        this.cancelFlag = cancelFlag;
    }

    private void pullRecords() {
        ConsumerRecords<String, String> records = getConsumer().poll(Duration.ofMillis(1000));
        for (ConsumerRecord record : records) {
            bufferedRecords.add(record);
        }
    }

    @Override
    public Object[] current() {
        Object[] row = new Object[5];
        row[0] = curRecord.partition();
        row[1] = curRecord.timestamp();
        row[2] = curRecord.offset();
        row[3] = curRecord.key();
        row[4] = curRecord.value();
        return row;
    }

    @Override
    public boolean moveNext() {
        if (cancelFlag.get()) {
            return false;
        }
        while (bufferedRecords.isEmpty()) {
            pullRecords();
        }
        curRecord = bufferedRecords.removeFirst();
        return true;
    }

    @Override
    public void reset() {
        if(consumer != null) {
            consumer.close();
            consumer = null;
        }
        this.bufferedRecords.clear();
        pullRecords();
    }

    @Override
    public void close() {
        if(consumer != null) {
            consumer.unsubscribe();
            consumer.close();
        }
    }

    private KafkaConsumer<String, String> getConsumer() {
        if(consumer != null) {
            return consumer;
        }
        Properties props = new Properties();
        props.put("auto.offset.reset", "earliest");
        Optional.ofNullable(config.getConsumerProperties()).ifPresent(i -> i.forEach(props::put));
        props.put("bootstrap.servers", config.getBootstrapServers());
        props.put("group.id", "OWL_" + topic + "_" + IdUtil.simpleUUID());
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> _consumer = new KafkaConsumer<>(props);
        _consumer.subscribe(Collections.singletonList(topic));
        consumer = _consumer;
        return consumer;
    }
}
