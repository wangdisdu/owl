package com.owl.integration.elasticsearch;

import com.owl.api.IntegrationBuilder;
import com.owl.api.IntegrationContext;
import com.owl.api.annotation.Integration;
import com.owl.integration.elasticsearch.calcite.ElasticsearchSchema;
import com.owl.integration.elasticsearch.calcite.ElasticsearchSchemaFactory;
import com.owl.integration.elasticsearch.calcite.ElasticsearchTable;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

@Integration(
        display = "Elasticsearch",
        description = "Elasticsearch Integration",
        sqlPlaceholder = "SELECT * FROM",
        icon = "Elasticsearch.svg"
)
public class ElasticsearchIntegration implements IntegrationBuilder<ElasticsearchConfig> {
    private IntegrationContext context;
    private ElasticsearchConfig config;
    private ElasticsearchSchema schema;

    @Override
    public void build(IntegrationContext context, ElasticsearchConfig config) throws Exception {
        this.context = context;
        this.config = config;
    }

    @Override
    public IntegrationContext getContext() {
        return context;
    }

    @Override
    public ElasticsearchConfig configure(Map<String, Object> map) {
        ElasticsearchConfig config = new ElasticsearchConfig();
        config.configure(map);
        return config;
    }

    @Override
    public ElasticsearchConnection connect() throws Exception {
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
        ElasticsearchSchemaFactory factory = new ElasticsearchSchemaFactory();
        schema = factory.create(rootSchema, "default", operand);
        for (String tableName: schema.getTableNames()) {
            ElasticsearchTable table = (ElasticsearchTable) schema.getTable(tableName);
            rootSchema.add(tableName, table);
        }

        ElasticsearchConnection result = new ElasticsearchConnection();
        result.setConnection(con);
        result.setSchema(schema.getIntegrationSchema());
        result.setIntegration(this);
        return result;
    }

    @Override
    public void close() throws IOException {
        if(schema != null) {
            schema.close();
        }
    }
}
