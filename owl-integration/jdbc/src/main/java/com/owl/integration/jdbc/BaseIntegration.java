package com.owl.integration.jdbc;

import cn.hutool.core.util.StrUtil;
import com.owl.api.IntegrationBuilder;
import com.owl.api.IntegrationConnection;
import com.owl.api.IntegrationContext;
import org.apache.calcite.adapter.jdbc.JdbcTable;
import org.apache.calcite.adapter.jdbc.OwlJdbcSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;


public abstract class BaseIntegration<T extends JdbcConfig> implements IntegrationBuilder<T> {
    private IntegrationContext context;
    private T config;
    private Schema schema;

    @Override
    public void build(IntegrationContext context, T config) throws Exception {
        this.context = context;
        this.config = config;
    }

    @Override
    public IntegrationContext getContext() {
        return context;
    }

    @Override
    public IntegrationConnection connect() throws Exception {

        Properties info = new Properties();
        // https://calcite.apache.org/docs/adapter.html#jdbc-connect-string-parameters
        if (StrUtil.isNotBlank(config.getLexical())) {
            info.setProperty("lex", config.getLexical());
        }
        if (config.getJdbcProperties() != null && !config.getJdbcProperties().isEmpty()) {
            info.putAll(config.getJdbcProperties());
        }
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:calcite:", info);
            CalciteConnection connection = con.unwrap(CalciteConnection.class);
            SchemaPlus rootSchema = connection.getRootSchema();
            Map<String, Object> operand = config.getParameters();
            OwlJdbcSchema.Factory factory = OwlJdbcSchema.Factory.INSTANCE;
            schema = factory.create(rootSchema, "default", operand);
            rootSchema.add("default", schema);
            JdbcSchemaReader reader = new JdbcSchemaReader(connection);
            for (String tableName: schema.getTableNames()) {
                JdbcTable table = (JdbcTable) schema.getTable(tableName);
                rootSchema.add(tableName, table);
                reader.readSchema(table);
            }
            JdbcConnection result = new JdbcConnection();
            result.setConnection(con);
            result.setSchema(reader.getSchema());
            result.setIntegration(this);
            return result;
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignore) {
                }
            }
            throw ex;
        }
    }

    @Override
    public void close() throws IOException {
    }
}
