package com.owl.integration.kafka.calcite;

import com.owl.integration.kafka.KafkaConfig;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerator;

import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaEnumerable extends AbstractEnumerable<Object[]>  {
    private final String topic;
    private final KafkaConfig config;
    private final AtomicBoolean cancelFlag;

    public KafkaEnumerable(String topic, KafkaConfig config, AtomicBoolean cancelFlag) {
        this.topic = topic;
        this.config = config;
        this.cancelFlag = cancelFlag;
    }

    @Override
    public Enumerator<Object[]> enumerator() {
        return new KafkaEnumerator(topic, config, cancelFlag);
    }
}
