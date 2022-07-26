package com.dmwa.dpg3.TransactionProcessing;

public interface ITransaction {
    void acquireLocks();
    void executeTransaction();
    void releaseLocks();
}
