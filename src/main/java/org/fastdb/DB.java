package org.fastdb;

import java.sql.SQLException;

import javax.persistence.PersistenceException;

public class DB {

	private static DBServer dbServer = DBConfig.getPrimaryDBServer();

	/**
	 * Persist an entity.
	 * <p>
	 * If there is no current transaction one will be created and committed for
	 * you automatically.
	 * </p>
	 * <p>
	 * Save can cascade along relationships. For this to happen you need to
	 * specify a cascade of CascadeType.ALL or CascadeType.PERSIST on the
	 * OneToMany, OneToOne or ManyToMany annotation.
	 * </p>
	 * 
	 * @param entity entity instance
	 * @throws PersistenceException if occurs any SQLException
	 */
	public static void persist(Object entity) {
		dbServer.persist(entity);
	}

	/**
	 * Delete an entity.
	 * 
	 * @param entity entity instance
	 * @throws PersistenceException if occurs any SQLException
	 */
	public static void delete(Object entity) {
		dbServer.delete(entity);
	}

	/**
	 * Find entity by primary key. Search for an entity of the specified class
	 * and primary key.
	 * 
	 * @param entityClass entity class
	 * @param primaryKey primary key
	 * @return the found entity instance or null if the entity does not exist
	 * @throws PersistenceException if occurs any SQLException
	 */
	public static <T> T find(Class<T> entityClass, Object primaryKey) {
		return dbServer.find(entityClass, primaryKey);
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

	/**
	 * Start a new transaction putting it into a ThreadLocal.
	 * 
	 * @throws SQLException
	 */
	public static Transaction beginTransaction() throws SQLException {
		return dbServer.beginTransaction();
	}

	/**
	 * Returns the current transaction or null if there is no current
	 * transaction in scope.
	 */
	public static Transaction currentTransaction() {
		return dbServer.currentTransaction();
	}

	/**
	 * Commit the current transaction.
	 */
	public static void commitTransaction() {
		dbServer.commitTransaction();
	}

	/**
	 * Rollback the current transaction.
	 */
	public static void rollbackTransaction() {
		dbServer.rollbackTransaction();
	}
}
