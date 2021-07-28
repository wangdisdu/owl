package com.owl.integration.jdbc;

import com.owl.api.schema.ColumnType;
import com.owl.api.schema.IntegrationSchema;
import com.owl.api.schema.JdbcType;
import com.owl.api.schema.TableColumn;
import com.owl.api.schema.TableSchema;
import org.apache.calcite.adapter.jdbc.JdbcTable;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcSchemaReader {
    private final IntegrationSchema schema = new IntegrationSchema();
    private final Connection connection;

    public JdbcSchemaReader(Connection connection) {
        this.connection = connection;
    }

    public IntegrationSchema getSchema() {
        return schema;
    }

    public void readSchema(JdbcTable table) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        TableSchema tableSchema = getRelDataType(
                metaData,
                table.jdbcCatalogName,
                table.jdbcSchemaName,
                table.jdbcTableName
        );
        schema.addTable(tableSchema);
    }

    public TableSchema getRelDataType(DatabaseMetaData metaData,
                                      String catalogName,
                                      String schemaName,
                                      String tableName) throws SQLException {
        final ResultSet resultSet =
                metaData.getColumns(catalogName, schemaName, tableName, null);
        TableSchema tableSchema = new TableSchema();
        tableSchema.setName(tableName);
        while (resultSet.next()) {
            final String columnName = resultSet.getString(4);
            final int dataType = resultSet.getInt(5);
            JdbcType jdbcType = JdbcType.fromJdbc(dataType);
            ColumnType columnType = ColumnType.fromJdbc(dataType);
            TableColumn column = new TableColumn();
            column.setName(columnName);
            column.setType(columnType);
            column.setJdbcType(jdbcType);
            tableSchema.addColumn(column);
        }
        resultSet.close();
        return tableSchema;
    }
}
