package com.owl.web.provider;

import com.owl.api.IntegrationContext;
import com.owl.api.annotation.IntegrationMeta;
import com.owl.common.JsonUtil;
import com.owl.core.BuilderConfig;
import com.owl.core.IntegrationCore;
import com.owl.web.common.ResponseCode;
import com.owl.web.common.config.AppConfig;
import com.owl.web.common.consts.ColumnType;
import com.owl.web.common.exception.BizException;
import com.owl.web.dao.entity.TbIntegration;
import com.owl.web.model.integration.Column;
import com.owl.web.model.integration.Dataset;
import com.owl.web.model.integration.IntegrationQuery;
import com.owl.web.model.integration.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class IntegrationCoreService {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationCoreService.class);

    public IntegrationContext context() {
        return new IntegrationContext();
    }

    public List<IntegrationMeta> scan() {
        try {
            return IntegrationCore.scan();
        } catch (Exception e) {
            logger.error("scan", e);
            throw new BizException(ResponseCode.FAILED, e);
        }
    }

    public Connection connect(TbIntegration integration) {
        try {
            BuilderConfig config = new BuilderConfig();
            config.setBuilder(integration.getBuilder());
            config.setConfig(JsonUtil.decode2Map(integration.getConfig()));
            return IntegrationCore.connect(context(), config);
        } catch (Exception e) {
            logger.error("connect", e);
            throw new BizException(ResponseCode.FAILED, e);
        }
    }

    public Dataset query(TbIntegration integration, IntegrationQuery query) {
        List<Column> columns = new ArrayList<>();
        List<Row> rows = new ArrayList<>();
        Dataset dataset = new Dataset();
        dataset.setColumns(columns);
        dataset.setRows(rows);
        try (Connection connection = connect(integration)) {
            try (Statement statement = connection.createStatement()) {
                statement.setMaxRows(AppConfig.MAX_QUERY_ROW_COUNT);
                try (ResultSet resultSet = statement.executeQuery(query.getSql())) {
                    ResultSetMetaData resultSetMeta = resultSet.getMetaData();
                    int columnCount = resultSetMeta.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = resultSetMeta.getColumnLabel(i);
                        int jdbcType = resultSetMeta.getColumnType(i);
                        ColumnType columnType = getColumnType(resultSet, i);
                        Column column = new Column();
                        column.setName(columnName);
                        column.setType(columnType);
                        column.setJdbcType(jdbcType);
                        columns.add(column);
                    }
                    while (resultSet.next()) {
                        Row row = new Row();
                        for (int i = 1; i <= columnCount; i++) {
                            String column = columns.get(i-1).getName();
                            Object value = getColumnValue(resultSet, i);
                            row.put(column, value);
                        }
                        rows.add(row);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("query", e);
            throw new BizException(ResponseCode.FAILED, e);
        }
        return dataset;
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

    public ColumnType getColumnType(ResultSet resultSet, int index) throws SQLException {
        int jdbcType = resultSet.getMetaData().getColumnType(index);
        switch (jdbcType) {
            case Types.BOOLEAN:
            case Types.BIT:
                return ColumnType.BOOL;
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
                return ColumnType.INT;
            case Types.BIGINT:
                return ColumnType.LONG;
            case Types.REAL:
                return ColumnType.FLOAT;
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return ColumnType.DOUBLE;
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.DATALINK:
            case Types.SQLXML:
                return ColumnType.STRING;
            case Types.DATE:
                return ColumnType.DATE;
            case Types.TIME:
                return ColumnType.TIME;
            case Types.TIMESTAMP:
                return ColumnType.TIMESTAMP;
            default:
                return ColumnType.OTHER;
        }
    }
}
