package com.owl.api.schema;

import java.util.ArrayList;
import java.util.List;

public class IntegrationSchema {
    private List<TableSchema> tables = new ArrayList<>();

    public List<TableSchema> getTables() {
        return tables;
    }

    public void setTables(List<TableSchema> tables) {
        this.tables = tables;
    }

    public void addTable(TableSchema table) {
        this.tables.add(table);
    }
}
