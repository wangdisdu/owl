/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.owl.integration.mongodb.calcite;

import com.google.common.collect.ImmutableMap;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoDatabase;
import com.owl.api.schema.IntegrationSchema;
import com.owl.api.schema.TableSchema;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.bson.Document;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * Schema mapped onto a directory of MONGO files. Each table in the schema
 * is a MONGO file in that directory.
 */
public class MongoSchema extends AbstractSchema implements Closeable {
    private final MongoClient client;
    private final MongoDatabase mongoDb;
    private final Map<String, Table> tableMap;
    private final IntegrationSchema integrationSchema = new IntegrationSchema();

    /**
     * Creates a MongoDB schema.
     *
     * @param host       Mongo host, e.g. "localhost"
     * @param credential Optional credentials (null for none)
     * @param options    Mongo connection options
     * @param database   Mongo database name, e.g. "foodmart"
     */
    MongoSchema(String host, String database,
                MongoCredential credential, MongoClientOptions options) {
        super();
        try {
            this.client = credential == null
                    ? new MongoClient(new ServerAddress(host), options)
                    : new MongoClient(new ServerAddress(host), credential, options);
            this.mongoDb = client.getDatabase(database);
            this.tableMap = createTables();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Map<String, Table> getTableMap() {
        return tableMap;
    }

    private Map<String, Table> createTables() {
        final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
        ListCollectionsIterable<Document> collections = mongoDb.listCollections();
        for (Document collection : collections) {
            collection.toString();
        }
        for (String collectionName : mongoDb.listCollectionNames()) {
            builder.put(collectionName, new MongoTable(collectionName, mongoDb));
            TableSchema tableSchema = new TableSchema();
            tableSchema.setName(collectionName);
            integrationSchema.addTable(tableSchema);
        }
        return builder.build();
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }

    public IntegrationSchema getIntegrationSchema() {
        return integrationSchema;
    }
}
