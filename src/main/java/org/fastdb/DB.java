package org.fastdb;

import javax.persistence.Query;

public class DB {

	private static DBServer dbServer;

	public static void delete(Object entity) {
		dbServer.delete(entity);
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
