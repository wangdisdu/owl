package com.owl.integration.mongodb;

import com.owl.api.IntegrationBuilder;
import com.owl.api.IntegrationConnection;
import com.owl.api.IntegrationContext;
import com.owl.api.annotation.Integration;
import com.owl.integration.mongodb.calcite.MongoSchema;
import com.owl.integration.mongodb.calcite.MongoSchemaFactory;
import com.owl.integration.mongodb.calcite.MongoTable;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

@Integration(
        display = "Mongodb",
        description = "Mongodb Integration",
        sqlPlaceholder = "SELECT * FROM",
        icon = "Mongodb.svg"
)
public class MongodbIntegration implements IntegrationBuilder<MongodbConfig> {
    private IntegrationContext context;
    private MongodbConfig config;
    private MongoSchema schema;


    @Override
    public void build(IntegrationContext context, MongodbConfig config) throws Exception {
        this.context = context;
        this.config = config;
    }

    @Override
    public MongodbConfig configure(Map<String, Object> map) {
        MongodbConfig config = new MongodbConfig();
        config.configure(map);
        return config;
    }

    @Override
    public IntegrationContext getContext() {
        return context;
    }

    @Override
    public IntegrationConnection connect() throws Exception {
        //init calcite jdbc driver
        Class.forName("org.apache.calcite.jdbc.Driver");
        Properties info = new Properties();
        // https://calcite.apache.org/docs/adapter.html#jdbc-connect-string-parameters
        info.setProperty("lex", "MYSQL");
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:calcite:", info);
            CalciteConnection connection = con.unwrap(CalciteConnection.class);
            SchemaPlus rootSchema = connection.getRootSchema();
            Map<String, Object> operand = config.getParameters();
            MongoSchemaFactory factory = new MongoSchemaFactory();
            schema = (MongoSchema) factory.create(rootSchema, "default", operand);
            rootSchema.add("default", schema);
            for (String tableName : schema.getTableNames()) {
                MongoTable table = (MongoTable) schema.getTable(tableName);
                rootSchema.add(tableName, table);
            }

            MongodbConnection result = new MongodbConnection();
            result.setConnection(con);
            result.setSchema(schema.getIntegrationSchema());
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
        if (schema != null) {
            schema.close();
        }
    }
}
