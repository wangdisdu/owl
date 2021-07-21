package com.owl.web.provider;

import com.owl.api.IntegrationConnection;
import com.owl.api.schema.DataFrame;
import com.owl.api.schema.TableColumn;
import com.owl.api.schema.TableRow;
import com.owl.web.common.config.AppConfig;
import com.owl.web.model.integration.IntegrationQuery;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class LoadDataFrameHandler implements ConnectionHandler {
    private final IntegrationQuery query;
    private final DataFrame dataFrame = new DataFrame();

    public LoadDataFrameHandler(IntegrationQuery query) {
        this.query = query;
    }

    public DataFrame getDataFrame() {
        return dataFrame;
    }

    @Override
    public void handle(IntegrationConnection connection) throws Exception {
        try (Statement statement = connection.getConnection().createStatement()) {
            statement.setMaxRows(AppConfig.MAX_QUERY_ROW_COUNT);
            try (ResultSet resultSet = statement.executeQuery(query.getSql())) {
                ResultSetMetaData resultSetMeta = resultSet.getMetaData();
                int columnCount = resultSetMeta.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSetMeta.getColumnLabel(i);
                    int jdbcType = resultSetMeta.getColumnType(i);
                    dataFrame.addColumn(TableColumn.build(columnName, jdbcType));
                }
                while (resultSet.next()) {
                    TableRow row = new TableRow();
                    for (int i = 1; i <= columnCount; i++) {
                        String column = dataFrame.getColumns().get(i - 1).getName();
                        Object value = getColumnValue(resultSet, i);
                        row.put(column, value);
                    }
                    dataFrame.addRow(row);
                }
            }
        }
    }


    private Object getColumnValue(ResultSet resultSet, int index) throws SQLException {
        Object obj = resultSet.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob) {
            Blob blob = (Blob) obj;
            obj = blob.getBytes(1, (int) blob.length());
        } else if (obj instanceof Clob) {
            Clob clob = (Clob) obj;
            obj = clob.getSubString(1, (int) clob.length());
        } else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
            obj = resultSet.getTimestamp(index);
        } else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String metaDataClassName = resultSet.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(metaDataClassName) || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
                obj = resultSet.getTimestamp(index);
            } else {
                obj = resultSet.getDate(index);
            }
        } else if (obj instanceof java.sql.Date) {
            if ("java.sql.Timestamp".equals(resultSet.getMetaData().getColumnClassName(index))) {
                obj = resultSet.getTimestamp(index);
            }
        }
        return obj;
    }
}
