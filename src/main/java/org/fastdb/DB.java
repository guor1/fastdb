package org.fastdb;

import javax.persistence.Query;

public class DB {

	private static DBServer dbServer;

	public static <T> void persist(T entity) {
		dbServer.persist(entity);
	}

	public static <T> T merge(T entity) {
		return dbServer.merge(entity);
	}

	public static <T> void remove(T entity) {
		dbServer.remove(entity);
	}

	public static <T> T find(Class<T> entityClass, Object primaryKey) {
		return dbServer.find(entityClass, primaryKey);
	}

	public static Query createQuery(String qlString) {
		return null;
	}

	/**
	 * create a query based on a manual sql
	 * 
	 * @param sql sql to be executed
	 * @return
	 */
	public static DBQuery createNativeQuery(String sqlString) {
		return dbServer.createNativeQuery(sqlString);
	}

	public static <T> Query createNativeQuery(String sqlString, Class<T> resultClass) {
		return null;
	}

	public static Query createNativeQuery(String sqlString, String resultSetMapping) {
		return null;
	}

	public static void close() {
	}

	public static boolean isOpen() {
		return false;
	}
}
