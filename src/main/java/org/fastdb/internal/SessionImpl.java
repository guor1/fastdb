package org.fastdb.internal;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import org.fastdb.FastdbException;
import org.fastdb.Session;
import org.fastdb.SessionFactory;

public class SessionImpl implements Session {

	private final Connection connection;

	private boolean closed;

	public SessionImpl(final Connection connection, final long timestamp, final boolean flushBeforeCompletionEnabled,
			final boolean autoCloseSessionEnabled, final String tenantIdentifier) {
		this.connection = connection;
	}

	@Override
	public Connection connection() {
		return this.connection;
	}

	@Override
	public void flush() {
	}

	@Override
	public SessionFactory getSessionFactory() {
		return null;
	}

	@Override
	public void close() throws FastdbException {
		try {
			this.connection.close();
		} catch (SQLException e) {
			throw new FastdbException("close connection failure.", e);
		}
	}

	@Override
	public void cancelQuery() throws FastdbException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOpen() {
		return !closed;
	}

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public boolean isDefaultReadOnly() {
		return false;
	}

	@Override
	public void setDefaultReadOnly(boolean readOnly) {
	}

	@Override
	public Serializable getIdentifier(Object object) {
		return null;
	}

	@Override
	public boolean contains(Object object) {
		return false;
	}

	@Override
	public void evict(Object object) {
	}

	@Override
	public <T> T load(Class<T> theClass, Serializable id) {
		return null;
	}

	@Override
	public Object load(String entityName, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable save(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable save(String entityName, Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrUpdate(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveOrUpdate(String entityName, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(String entityName, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object merge(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object merge(String entityName, Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persist(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void persist(String entityName, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String entityName, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(String entityName, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T get(Class<T> clazz, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get(String entityName, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

}
