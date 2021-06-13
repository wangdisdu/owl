package com.owl.integration.kafka.calcite;

import com.google.common.collect.ImmutableList;
import com.owl.integration.kafka.KafkaConfig;
import org.apache.calcite.DataContext;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.*;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A table that maps to an Apache Kafka topic.
 *
 * <p>Currently only {@link KafkaStreamTable} is
 * implemented as a STREAM table.
 */
public class KafkaStreamTable implements ScannableTable, StreamableTable {
    private final String topic;
    private final KafkaConfig config;

    public KafkaStreamTable(String topic, KafkaConfig config) {
        this.topic = topic;
        this.config = config;
    }

    @Override
    public Enumerable<Object[]> scan(final DataContext root) {
        final AtomicBoolean cancelFlag = DataContext.Variable.CANCEL_FLAG.get(root);
        return new KafkaEnumerable(topic, config, cancelFlag);
    }

    @Override
    public RelDataType getRowType(final RelDataTypeFactory typeFactory) {
        final RelDataTypeFactory.Builder builder = typeFactory.builder();
        builder.add("PARTITION", typeFactory.createSqlType(SqlTypeName.INTEGER)).nullable(true);
        builder.add("TIMESTAMP", typeFactory.createSqlType(SqlTypeName.BIGINT)).nullable(true);
        builder.add("OFFSET", typeFactory.createSqlType(SqlTypeName.BIGINT)).nullable(true);
        builder.add("KEY", typeFactory.createSqlType(SqlTypeName.VARCHAR)).nullable(true);
        builder.add("VALUE", typeFactory.createSqlType(SqlTypeName.VARCHAR)).nullable(true);
        return builder.build();
    }

    @Override
    public Statistic getStatistic() {
        return Statistics.of(100d, ImmutableList.of(),
                RelCollations.createSingleton(0));
    }

    @Override
    public boolean isRolledUp(final String column) {
        return false;
    }

    @Override
    public boolean rolledUpColumnValidInsideAgg(
            final String column,
            final SqlCall call,
            final SqlNode parent,
            final CalciteConnectionConfig config
    ) {
        return false;
    }

    @Override
    public Table stream() {
        return this;
    }

    @Override
    public Schema.TableType getJdbcTableType() {
        return Schema.TableType.STREAM;
    }
}
