package com.dmwa.dpg3.TransactionProcessing;

import com.dmwa.dpg3.TransactionProcessing.enums.LockStatus;

import java.util.*;
import java.util.concurrent.locks.Lock;

public class LockManager {
    private Map<String, LinkedList<LockRequest>> lockTable;

    public LockManager() {
        lockTable = new HashMap<>();
    }

    void requestLock(LockRequest lockRequest) {
        //If there is no lock on the current table, make a new entry for the table inside the map, and assign the status as GRANTED to lock request.
        if (!lockTable.containsKey(lockRequest.getLockRequestOn())) {
            lockTable.put(lockRequest.getLockRequestOn(), new LinkedList<>());
            lockRequest.setLockStatus(LockStatus.GRANTED);
        } else //If there is already a lock inside the table, mark the current lock request as WAITING.
             {
            lockRequest.setLockStatus(LockStatus.WAITING);
        }

        lockTable.computeIfPresent(lockRequest.getLockRequestOn(),
                (k, v) -> {
                    v.add(lockRequest);
                    return v;
                });
    }

    void deleteLock(LockRequest lockRequest) {
        lockTable.computeIfPresent(lockRequest.getLockRequestOn(), (k, v) ->
        {
            if (v.size() > 0) {
                v.remove(0);
                if (v.size() > 0) {
                    v.get(0).setLockStatus(LockStatus.GRANTED);
                }
            }
            return v;
        });
    }
}
