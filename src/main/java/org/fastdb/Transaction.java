package org.fastdb;

import java.sql.Connection;

import javax.persistence.RollbackException;

public interface Transaction {
	public void commit() throws RollbackException;

	public void rollback() throws RollbackException;
	
	Connection getConnection();
}
