package com.owl.integration.elasticsearch;

import cn.hutool.core.util.StrUtil;
import com.owl.api.IntegrationBuilder;
import com.owl.api.IntegrationContext;
import com.owl.api.annotation.Integration;
import com.owl.common.JsonUtil;
import org.apache.calcite.adapter.elasticsearch.ElasticsearchSchemaFactory;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Integration(
        display = "Elasticsearch",
        description = "Elasticsearch Integration"
)
public class ElasticsearchIntegration implements IntegrationBuilder<ElasticsearchConfig> {
    private IntegrationContext context;
    private ElasticsearchConfig config;

    @Override
    public void build(IntegrationContext context, ElasticsearchConfig config) throws Exception {
        this.context = context;
        this.config = config;
    }

    @Override
    public ElasticsearchConfig configure(Map<String, Object> map) {
        ElasticsearchConfig config = new ElasticsearchConfig();
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
        Map<String, Object> operand = new HashMap<>();
        operand.put("hosts", JsonUtil.encode(StrUtil.split(config.getHosts(),',')));
        operand.put("index", config.getIndex());
        ElasticsearchSchemaFactory factory = new ElasticsearchSchemaFactory();
        Schema schema = factory.create(rootSchema, "default", operand);
        for (String tableName: schema.getTableNames()) {
            org.apache.calcite.schema.Table t = schema.getTable(tableName);
            rootSchema.add(tableName, t);
        }

        rootSchema.add("default", schema);
        return con;
    }
}
