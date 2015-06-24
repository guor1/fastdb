package org.fastdb.internal;

import java.util.HashMap;
import java.util.Map;

import org.fastdb.Transaction;

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
}
