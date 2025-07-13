package com.jpmc.midascore.messaging;

import com.jpmc.midascore.foundation.Transaction;

public interface TransactionService {

    /**
     * Validate and record a new transaction.
     */
    void onReceive(Transaction transaction);
}
