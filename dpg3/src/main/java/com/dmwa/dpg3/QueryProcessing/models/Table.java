package com.dmwa.dpg3.QueryProcessing.models;

import java.util.List;

/**
 * @author Pavithra Gunasekaran
 */
public class Table {

    private String tableName;
    private String databaseName;
    public List<String> columns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
