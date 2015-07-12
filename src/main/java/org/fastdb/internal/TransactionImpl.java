package org.fastdb.internal;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.fastdb.Transaction;

public class TransactionImpl implements Transaction {

	private Connection connection;

	public TransactionImpl(Connection connection) {
		this.connection = connection;
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void commit() {
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new RollbackException(e);
		}
	}

	@Override
	public void rollback() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new RollbackException(e);
		}
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}
}
