package org.fastdb;

public interface SessionFactory {
	public Session openSession() throws FastdbException;

	public Session getCurrentSession() throws FastdbException;

	public void close() throws FastdbException;

	public boolean isClosed();
}