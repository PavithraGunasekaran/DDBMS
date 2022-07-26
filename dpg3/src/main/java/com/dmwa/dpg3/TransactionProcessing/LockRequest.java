package com.dmwa.dpg3.TransactionProcessing;

import com.dmwa.dpg3.TransactionProcessing.enums.LockType;
import com.dmwa.dpg3.TransactionProcessing.enums.LockStatus;

public class LockRequest {
    private String lockRequestOn;
    private LockType lockType;
    private LockStatus lockStatus;
    public LockRequest(String lockRequestOn,  LockType lockType) {
        this.lockRequestOn = lockRequestOn;
        this.lockType = lockType;
    }

    public String getLockRequestOn() {
        return lockRequestOn;
    }

    public LockType getLockType() {
        return lockType;
    }

    public LockStatus getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(LockStatus lockStatus) {
        this.lockStatus = lockStatus;
    }
}
