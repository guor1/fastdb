package org.fastdb;

public interface SessionFactory {
	public Session openSession() throws FastdbException;

	public Session getCurrentSession() throws FastdbException;

	public <T> ClassMetadata getClassMetadata(Class<T> entityClass);

	public ClassMetadata getClassMetadata(String entityName);

	public void close() throws FastdbException;

	public boolean isClosed();
}