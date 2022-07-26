package com.dmwa.dpg3.DBLogger.models;

import org.json.simple.JSONObject;

public class UserLog {
    private String user;
    private int queryCount;

    public UserLog() {

    }

    String brackets= String.valueOf('"');
    @Override
    public String toString() {
        return  "{" +brackets+
                "user"+brackets+":" + brackets+user+brackets +
                ","+brackets+"queryCount"+brackets+":" + queryCount+
                "}"
                ;
    }

    public UserLog(String user) {
        this.user = user;
        this.queryCount = 1;
    }

    public String getUser() {
        return user;
    }

    public int getQueryCount() {
        return queryCount;
    }

    public void addQueryCount(int queryCount) {
        this.queryCount += queryCount;
    }

    public JSONObject getUserJsonObject() {
        var userJson = new JSONObject();
        userJson.put("user", user);
        userJson.put("queryCount", queryCount);
        return userJson;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setQueryCount(int queryCount) {
        this.queryCount = queryCount;
    }
}
