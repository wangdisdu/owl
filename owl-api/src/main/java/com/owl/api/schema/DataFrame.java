package com.owl.api.schema;

import java.util.ArrayList;
import java.util.List;

public class DataFrame {
    private List<TableColumn> columns = new ArrayList<>();
    private List<TableRow> rows = new ArrayList<>();

    public List<TableColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<TableColumn> columns) {
        this.columns = columns;
    }

    public void addColumn(TableColumn column) {
        this.columns.add(column);
    }

    public List<TableRow> getRows() {
        return rows;
    }

    public void setRows(List<TableRow> rows) {
        this.rows = rows;
    }

    public void addRow(TableRow row) {
        this.rows.add(row);
    }
}
