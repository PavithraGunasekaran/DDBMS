package com.dmwa.dpg3.DBLogger.models;


import com.dmwa.dpg3.DBLogger.enums.VMInstance;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DBLog {
    private String dbName;

    String brackets= String.valueOf('"');
    @Override
    public String toString() {
        return  "{"+brackets+
                "dbName"+brackets+":" + brackets+dbName+brackets +
                ","+brackets+"vmInstance"+brackets+":" + brackets+vmInstance+brackets +
                ","+brackets+"activeUsers"+brackets+":" + activeUsers.values() +
                ","+brackets+"tableMap"+brackets+":" + tableMap.values() +
                '}';
    }

    private VMInstance vmInstance;
    private Map<String, UserLog> activeUsers;
    private Map<String, TableLog> tableMap;

    public DBLog(){}

    public DBLog(String dbName, VMInstance vmInstance) {
        this.dbName = dbName;
        this.vmInstance = vmInstance;
        this.activeUsers = new HashMap<>();
        this.tableMap = new HashMap<>();
    }

    public String getDbName() {
        return dbName;
    }

    public VMInstance getVmInstance() {
        return vmInstance;
    }

    public Map<String, TableLog> getTableMap() {
        return tableMap;
    }

    public void addOrUpdateTableInfo(String tableName, QueryLog queryLog) {
        if (!this.tableMap.containsKey(tableName)) {
            this.tableMap.put(tableName, new TableLog(tableName));
        }
        this.tableMap.compute(tableName, (k, v) -> {
            v.addQuery(queryLog);
            v.setNumberOfRecords(getNumberOfRecordsToUpdateInTable(queryLog));
            v.addOrUpdateQueryTypeCount(queryLog.getQueryType(), 1);
            return v;
        });
    }

    public Map<String, UserLog> getActiveUsers() {
        return activeUsers;
    }

    public void addOrUpdateActiveUser(String user) {
        if (!this.activeUsers.containsKey(user))
            this.activeUsers.put(user, new UserLog(user));
        this.activeUsers.compute(user, (k, v) -> {
            v.addQueryCount(v.getQueryCount());
            return v;
        });
    }

    private int getNumberOfRecordsToUpdateInTable(QueryLog queryLog) {
        switch (queryLog.getQueryType()) {
            case INSERT:
                return queryLog.getNumberOfRecords();
            case DELETE:
                return -queryLog.getNumberOfRecords();
        }
        return 0;
    }

    public JSONObject getDBJsonObject() {

        var dbJson = new JSONObject();
        dbJson.put("dbName", dbName);
        dbJson.put("vmInstance", vmInstance.toString());

        var activeUsersJson = new JSONArray();
        for (var user : activeUsers.keySet()) {
            activeUsersJson.add(activeUsers.get(user).getUserJsonObject());
        }
        dbJson.put("activeUsers", activeUsersJson);

        var tablesJson = new JSONArray();
        for (var table : tableMap.keySet()) {
            tablesJson.add(tableMap.get(table).getTableJsonObject());
        }
        dbJson.put("tableMap", tablesJson);
        return dbJson;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setVmInstance(VMInstance vmInstance) {
        this.vmInstance = vmInstance;
    }

    public void setActiveUsers(Map<String, UserLog> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public void setTableMap(Map<String, TableLog> tableMap) {
        this.tableMap = tableMap;
    }
}
