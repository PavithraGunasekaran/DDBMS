package com.dmwa.dpg3.DBLogger.models;

import com.dmwa.dpg3.DBLogger.enums.QueryType;
import org.json.simple.JSONObject;

public class QueryLog {

    private String queryTimeStamp;
    private String queryExecutionTime;
    private QueryType queryType;
    private String queryText;
    private int numberOfRecords;
    private String user;

    public QueryLog() {
    }

    public QueryLog(String queryTimeStamp, String queryExecutionTime, QueryType queryType, String queryText, int numberOfRecords, String user) {
        this.queryTimeStamp = queryTimeStamp;
        this.queryExecutionTime = queryExecutionTime;
        this.queryType = queryType;
        this.queryText = queryText;
        this.numberOfRecords = numberOfRecords;
        this.user = user;
    }

    public String getQueryTimeStamp() {
        return queryTimeStamp;
    }

    public String getQueryExecutionTime() {
        return queryExecutionTime;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public String getQueryText() {
        return queryText;
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public String getUser() {
        return user;
    }

    public JSONObject getQueryJsonObject() {
        var queryJsonObject = new JSONObject();
        queryJsonObject.put("queryTimeStamp", queryTimeStamp);
        queryJsonObject.put("queryExecutionTime", queryExecutionTime);
        queryJsonObject.put("queryType", queryType.toString());
        queryJsonObject.put("queryText", queryText);
        queryJsonObject.put("numberOfRecords", numberOfRecords);
        queryJsonObject.put("user", user);

        return queryJsonObject;
    }

    String brackets= String.valueOf('"');
    @Override
    public String toString() {
        return "{" +brackets+
                "queryTimeStamp"+brackets+":" + brackets+queryTimeStamp+brackets +
                ","+brackets+"queryType"+brackets+":" + brackets+queryType+brackets +
                ","+brackets+"queryText"+brackets+":" + brackets+queryText+brackets +
                ","+brackets+"numberOfRecords"+brackets+":" + numberOfRecords +
                ","+brackets+"user"+brackets+":" + brackets+user+brackets +
                ","+brackets+"queryExecutionTime"+brackets+":" + brackets+queryExecutionTime+brackets +
                '}';
    }
}
