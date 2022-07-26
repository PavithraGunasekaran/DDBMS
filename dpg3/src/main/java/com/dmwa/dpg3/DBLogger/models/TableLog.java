package com.dmwa.dpg3.DBLogger.models;

import com.dmwa.dpg3.DBLogger.enums.QueryType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableLog {
    private String tableName;
    private int numberOfRecords;
    private List<QueryLog> queries;
    private Map<QueryType, Integer> queryTypeCount;

    public TableLog() {
    }

    String brackets= String.valueOf('"');
    @Override
    public String toString() {
        return "{" +brackets+
                "tableName"+brackets+":" + brackets+tableName+brackets +
                ","+brackets+"numberOfRecords"+brackets+":" +numberOfRecords +
                ","+brackets+"queries"+brackets+":" + queries.toString() +
                ","+brackets+"queryTypeCount"+brackets+":" + "{"+brackets+
                queryTypeCount.keySet().toString().replaceAll("\\[|\\]|,", "")
                +brackets+":"+queryTypeCount.values().toString().replaceAll("\\[|\\]|,", "") +
                "}}";
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setQueries(List<QueryLog> queries) {
        this.queries = queries;
    }

    public void setQueryTypeCount(Map<QueryType, Integer> queryTypeCount) {
        this.queryTypeCount = queryTypeCount;
    }

    public TableLog(String tableName) {
        this.tableName = tableName;
        this.numberOfRecords = 0;
        this.queries = new ArrayList<>();
        this.queryTypeCount = new HashMap<>();
    }

    public String getTableName() {
        return tableName;
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public List<QueryLog> getTableQueries() {
        return queries;
    }

    public void addQuery(QueryLog query) {
        this.queries.add(query);
    }

    public Map<QueryType, Integer> getQueryTypeCount() {
        return queryTypeCount;
    }

    public void addOrUpdateQueryTypeCount(QueryType queryType, int count) {
        if (!queryTypeCount.containsKey(queryType))
            this.queryTypeCount.put(queryType, 0);
        this.queryTypeCount.compute(queryType, (k, v) -> v + count);
    }

    public JSONObject getTableJsonObject() {
        var tableJson = new JSONObject();
        tableJson.put("tableName", tableName);
        tableJson.put("numberOfRecords", numberOfRecords);

        var queryTypeJson = new JSONObject(queryTypeCount);
        tableJson.put("queryTypeCount", queryTypeJson);

        var queriesJson = new JSONArray();
        for(var query : queries) {
            queriesJson.add(query.getQueryJsonObject());
        }
        tableJson.put("queries", queriesJson);

        return tableJson;
    }
}
