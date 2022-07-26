package com.dmwa.dpg3.TransactionProcessing;

import com.dmwa.dpg3.TransactionProcessing.enums.LockType;
import com.dmwa.dpg3.TransactionProcessing.enums.QueryType;

public class Query {
    private String text;
    private QueryType queryType;
    private LockType requiredLock;
    private String tableName;
    private LockRequest requestedLock;

    public Query(String text, QueryType queryType, LockType requiredLock, String tableName) {
        this.text = text;
        this.queryType = queryType;
        this.requiredLock = requiredLock;
        this.tableName = tableName;
    }

    public String getText() {
        return text;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public LockType getRequiredLock() {
        return requiredLock;
    }

    public String getTableName() {
        return tableName;
    }

    public LockRequest getRequestedLock() {
        return requestedLock;
    }

    public void setRequestedLock(LockRequest requestedLock) {
        this.requestedLock = requestedLock;
    }
}
