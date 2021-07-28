package com.owl.integration.kafka.calcite;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;

import java.util.ArrayList;
import java.util.List;

public interface KafkaRelNode extends RelNode {

    void implement(Implementor implementor);

    /**
     * Calling convention for relational operations that occur in Kafka.
     */
    Convention CONVENTION = new Convention.Impl("KAFKA", KafkaRelNode.class);

    default List<String> fieldNames(RelDataType relDataType) {
        List<String> names = new ArrayList<>();
        for (RelDataTypeField field : relDataType.getFieldList()) {
            names.add(field.getName());
        }
        return names;
    }

    enum KafkaAggCall {
        CountPartition,
        MaxOffset,
        MinOffset,
    }

    class Implementor {
        KafkaTable kafkaTable;

        private Long offsetFrom;
        private Long offsetTo;
        private Long limit;
        private List<Integer> partitions = new ArrayList<>();
        private List<KafkaAggCall> agg = new ArrayList<>();

        void visitChild(int ordinal, RelNode input) {
            assert ordinal == 0;
            ((KafkaRelNode) input).implement(this);
        }

        public Long getOffsetFrom() {
            return offsetFrom;
        }

        public Implementor setOffsetFrom(Long offsetFrom) {
            this.offsetFrom = offsetFrom;
            return this;
        }

        public Long getOffsetTo() {
            return offsetTo;
        }

        public Implementor setOffsetTo(Long offsetTo) {
            this.offsetTo = offsetTo;
            return this;
        }

        public Long getLimit() {
            return limit;
        }

        public Implementor setLimit(Long limit) {
            this.limit = limit;
            return this;
        }

        public List<Integer> getPartitions() {
            return partitions;
        }

        public Implementor setPartitions(List<Integer> partitions) {
            this.partitions = partitions;
            return this;
        }

        public Implementor addPartition(Integer partition) {
            this.partitions.add(partition);
            return this;
        }

        public List<KafkaAggCall> getAgg() {
            return agg;
        }

        public Implementor setAgg(List<KafkaAggCall> agg) {
            this.agg = agg;
            return this;
        }

        public Implementor addAgg(KafkaAggCall call) {
            agg.add(call);
            return this;
        }
    }
}
