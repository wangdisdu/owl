package com.owl.integration.kafka;

import com.owl.api.IntegrationBuilder;
import com.owl.api.IntegrationContext;
import com.owl.api.annotation.Integration;
import com.owl.integration.kafka.calcite.KafkaSchema;
import com.owl.integration.kafka.calcite.KafkaSchemaFactory;
import com.owl.integration.kafka.calcite.KafkaStreamTable;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

@Integration(
        display = "Kafka",
        description = "Kafka Integration",
        sqlPlaceholder = "SELECT STREAM * FROM",
        icon = "Kafka.svg"
)
public class KafkaIntegration implements IntegrationBuilder<KafkaConfig> {
    private IntegrationContext context;
    private KafkaConfig config;
    private KafkaSchema schema;

    @Override
    public void build(IntegrationContext context, KafkaConfig config) throws Exception {
        this.context = context;
        this.config = config;
    }

    @Override
    public IntegrationContext getContext() {
        return context;
    }

    @Override
    public KafkaConfig configure(Map<String, Object> map) {
        KafkaConfig config = new KafkaConfig();
        config.configure(map);
        return config;
    }

    @Override
    public KafkaConnection connect() throws Exception {
        //init calcite jdbc driver
        Class.forName("org.apache.calcite.jdbc.Driver");
        Properties info = new Properties();
        // https://calcite.apache.org/docs/adapter.html#jdbc-connect-string-parameters
        info.setProperty("lex", "MYSQL");
        info.setProperty("FUN", "MYSQL");
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:calcite:", info);
            CalciteConnection connection = con.unwrap(CalciteConnection.class);
            SchemaPlus rootSchema = connection.getRootSchema();
            Map<String, Object> operand = config.getParameters();
            KafkaSchemaFactory factory = new KafkaSchemaFactory();
            schema = factory.create(rootSchema, "default", operand);
            rootSchema.add("default", schema);
            for (String tableName : schema.getTableNames()) {
                KafkaStreamTable table = (KafkaStreamTable) schema.getTable(tableName);
                rootSchema.add(tableName, table);
            }
            KafkaConnection result = new KafkaConnection();
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
