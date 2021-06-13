package com.owl.integration.kafka;

import com.owl.api.IntegrationBuilder;
import com.owl.api.IntegrationContext;
import com.owl.api.annotation.Integration;
import com.owl.integration.kafka.calcite.KafkaSchemaFactory;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

@Integration(
        display = "Kafka",
        description = "Kafka Integration",
        sqlPlaceholder = "SELECT STREAM * FROM `TOPIC_NAME`"
)
public class KafkaIntegration implements IntegrationBuilder<KafkaConfig> {
    private IntegrationContext context;
    private KafkaConfig config;

    @Override
    public void build(IntegrationContext context, KafkaConfig config) throws Exception {
        this.context = context;
        this.config = config;
    }

    @Override
    public KafkaConfig configure(Map<String, Object> map) {
        KafkaConfig config = new KafkaConfig();
        config.configure(map);
        return config;
    }

    @Override
    public Connection connect() throws Exception {
        //init calcite jdbc driver
        Class.forName("org.apache.calcite.jdbc.Driver");
        Properties info = new Properties();
        // https://calcite.apache.org/docs/adapter.html#jdbc-connect-string-parameters
        info.setProperty("lex", "MYSQL");
        info.setProperty("FUN", "MYSQL");
        Connection con = DriverManager.getConnection("jdbc:calcite:", info);
        CalciteConnection connection = con.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = connection.getRootSchema();
        Map<String, Object> operand = config.getParameters();
        KafkaSchemaFactory factory = new KafkaSchemaFactory();
        Schema schema = factory.create(rootSchema, "default", operand);
        for (String tableName: schema.getTableNames()) {
            org.apache.calcite.schema.Table t = schema.getTable(tableName);
            rootSchema.add(tableName, t);
        }
        return con;
    }
}
