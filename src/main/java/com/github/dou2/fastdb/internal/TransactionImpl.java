package com.github.dou2.fastdb.internal;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.PersistenceException;

import com.github.dou2.fastdb.Transaction;

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
	public void commit() throws PersistenceException {
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e);
			}
		}
	}

	@Override
	public void rollback() throws PersistenceException {
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e);
			}
		}
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}
}
