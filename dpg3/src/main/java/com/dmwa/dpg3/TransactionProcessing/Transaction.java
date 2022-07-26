package com.dmwa.dpg3.TransactionProcessing;

import com.dmwa.dpg3.QueryProcessing.QueryExecution;
import com.dmwa.dpg3.QueryProcessing.Utils.TableUtility;
import com.dmwa.dpg3.TransactionProcessing.enums.LockType;
import com.dmwa.dpg3.TransactionProcessing.enums.QueryType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transaction implements ITransaction, Runnable {
    private String transactionName;
    private LockManager lockManager;
    private List<Query> queries;
    private LockRequest lockRequest;

    public Transaction(LockManager lockManager, String transactionText, String transactionName) {
        queries = new ArrayList<>();
        splitTransactionToQueries(transactionText);
        this.transactionName = transactionName;
        this.lockManager = lockManager;
    }

    private void splitTransactionToQueries(String transactionText) {
        List<String> queriesText = Arrays.asList(transactionText.split("\n"));
        for (var currentQuery : queriesText) {
            try {
                if (currentQuery.startsWith(QueryType.SELECT.name())) {
                    queries.add(new Query(currentQuery, QueryType.SELECT, LockType.SHARED, TableUtility.getTableName(currentQuery)));
                } else if (currentQuery.startsWith(QueryType.UPDATE.name())) {
                    queries.add(new Query(currentQuery, QueryType.UPDATE, LockType.EXCLUSIVE, TableUtility.getTableName(currentQuery)));
                } else if (currentQuery.startsWith(QueryType.DELETE.name())) {
                    queries.add(new Query(currentQuery, QueryType.DELETE, LockType.EXCLUSIVE, TableUtility.getTableName(currentQuery)));
                } else {
                    queries.add(new Query(currentQuery, QueryType.OTHER, LockType.NONE, TableUtility.getTableName(currentQuery)));
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void acquireLocks() {
        System.out.println(this.transactionName + ": Came for Locks acquiring.");

        for (var query : queries) {
            if (query.getRequiredLock() != null && query.getRequiredLock() == LockType.EXCLUSIVE) {
                lockRequest = new LockRequest(query.getTableName(),
                        LockType.EXCLUSIVE);
                lockManager.requestLock(lockRequest);
                query.setRequestedLock(lockRequest);
            } else if (query.getRequiredLock() != null && query.getRequiredLock() == LockType.SHARED) {
                lockRequest = new LockRequest(query.getTableName(),
                        LockType.SHARED);
                lockManager.requestLock(lockRequest);
                query.setRequestedLock(lockRequest);
            }
        }
        System.out.println(this.transactionName + ": Locks acquired.");
    }

    @Override
    public void executeTransaction() {
        try {
            for (var query : queries) {
                if (query.getRequestedLock() != null) {
                    lockManager.deleteLock(query.getRequestedLock());
                    query.setRequestedLock(null);
                }
                new QueryExecution();
                QueryExecution.executeQuery(query.getText());
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void releaseLocks() {
        System.out.println(this.transactionName + ": Came for Locks releasing.");
        for(var query: queries) {
            if (query.getRequestedLock() != null) {
                lockManager.deleteLock(query.getRequestedLock());
                query.setRequestedLock(null);
            }
        }
        System.out.println(this.transactionName + ": Locks released.");
    }

    @Override
    public void run() {
        acquireLocks();
        executeTransaction();
        releaseLocks();
        System.out.println();
    }
}
