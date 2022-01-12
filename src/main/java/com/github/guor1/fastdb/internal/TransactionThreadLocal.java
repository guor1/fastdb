package com.github.guor1.fastdb.internal;

import com.github.guor1.fastdb.Transaction;

import java.util.HashMap;
import java.util.Map;

public class TransactionThreadLocal {
    private static ThreadLocal<Map<String, Transaction>> local = new ThreadLocal<Map<String, Transaction>>() {
        @Override
        protected Map<String, Transaction> initialValue() {
            return new HashMap<String, Transaction>();
        }
    };

    public static Transaction get(String serverName) {
        Transaction tx = local.get().get(serverName);
        return tx;
    }

    /**
     * Set a new Transaction for this serverName and Thread.
     */
    public static void put(String serverName, Transaction tx) {
        local.get().put(serverName, tx);
    }

    public static void commit(String serverName) {
        Map<String, Transaction> map = local.get();
        Transaction tx = map.remove(serverName);
        if (tx == null) {
            throw new IllegalStateException("No current transaction for [" + serverName + "]");
        }
        tx.commit();
    }

    public static void rollback(String serverName) {
        Map<String, Transaction> map = local.get();
        Transaction tx = map.remove(serverName);
        if (tx == null) {
            throw new IllegalStateException("No current transaction for [" + serverName + "]");
        }
        tx.rollback();
    }
}
