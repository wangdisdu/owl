package com.owl.integration.kafka.calcite;

import com.google.common.collect.ImmutableList;
import com.owl.api.schema.TableColumn;
import com.owl.api.schema.TableSchema;
import com.owl.integration.kafka.KafkaConfig;
import org.apache.calcite.DataContext;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Statistics;
import org.apache.calcite.schema.StreamableTable;
import org.apache.calcite.schema.Table;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.type.SqlTypeName;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Types;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A table that maps to an Apache Kafka topic.
 *
 * <p>Currently only {@link KafkaStreamTable} is
 * implemented as a STREAM table.
 */
public class KafkaStreamTable implements ScannableTable, StreamableTable, Closeable {
    private final String topic;
    private final TableSchema tableSchema;
    private final KafkaConfig config;

    public KafkaStreamTable(String topic, KafkaConfig config) {
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
        tableSchema.addColumn(TableColumn.build("PARTITION", Types.INTEGER));
        tableSchema.addColumn(TableColumn.build("TIMESTAMP", Types.BIGINT));
        tableSchema.addColumn(TableColumn.build("OFFSET", Types.BIGINT));
        tableSchema.addColumn(TableColumn.build("KEY", Types.VARCHAR));
        tableSchema.addColumn(TableColumn.build("VALUE", Types.VARCHAR));
        return tableSchema;
    }

    @Override
    public Enumerable<Object[]> scan(final DataContext root) {
        final AtomicBoolean cancelFlag = DataContext.Variable.CANCEL_FLAG.get(root);
        return new KafkaEnumerable(topic, config, cancelFlag);
    }

    @Override
    public RelDataType getRowType(final RelDataTypeFactory relDataTypeFactory) {
        final RelDataTypeFactory.Builder builder = relDataTypeFactory.builder();
        for (TableColumn column : tableSchema.getColumns()) {
            SqlTypeName sqlTypeName = SqlTypeName.getNameForJdbcType(column.getJdbcType().code);
            RelDataType relDataType = relDataTypeFactory.createSqlType(sqlTypeName);
            builder.add(column.getName(), relDataType).nullable(true);
        }
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

    @Override
    public void close() throws IOException {
        // no need to close
    }
}
