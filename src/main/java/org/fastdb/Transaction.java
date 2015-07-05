package org.fastdb;

import java.sql.Connection;

public interface Transaction {
	/**
	 * Commit the current transaction
	 */
	public void commit();

	/**
	 * Roll back the current transaction
	 */
	public void rollback();

	/**
	 * get the connection on the current transaction
	 * 
	 * @return
	 */
	Connection getConnection();
}
