package org.fastdb;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.PersistenceException;

/**
 * Provide static methods that proxy for the <em>'default'</em> DBServer.
 * 
 * @author guor
 *
 */
public class DB {

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
		DBConfig.getPrimaryDBServer().persist(entity);
	}

	/**
	 * Delete an entity.
	 * 
	 * @param entity entity instance
	 * @throws PersistenceException if occurs any SQLException
	 */
	public static void delete(Object entity) {
		DBConfig.getPrimaryDBServer().delete(entity);
	}

	/**
	 * Find all entities of the specified class
	 * 
	 * @param entityClass
	 * @return
	 */
	public static <T> List<T> findList(Class<T> entityClass) {
		return DBConfig.getPrimaryDBServer().findList(entityClass);
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
		return DBConfig.getPrimaryDBServer().find(entityClass, primaryKey);
	}

	/**
	 * create a query based on a manual sql
	 * 
	 * @param sql sql to be executed
	 * @return
	 */
	public static DBQuery createNativeQuery(String sqlString) {
		return DBConfig.getPrimaryDBServer().createNativeQuery(sqlString);
	}

	/**
	 * Start a new transaction putting it into a ThreadLocal.
	 * 
	 * @throws SQLException
	 */
	public static Transaction beginTransaction() throws SQLException {
		return DBConfig.getPrimaryDBServer().beginTransaction();
	}

	/**
	 * Returns the current transaction or null if there is no current
	 * transaction in scope.
	 */
	public static Transaction currentTransaction() {
		return DBConfig.getPrimaryDBServer().currentTransaction();
	}

	/**
	 * Commit the current transaction.
	 */
	public static void commitTransaction() {
		DBConfig.getPrimaryDBServer().commitTransaction();
	}

	/**
	 * Rollback the current transaction.
	 */
	public static void rollbackTransaction() {
		DBConfig.getPrimaryDBServer().rollbackTransaction();
	}

	/**
	 * use DBServer by serverName
	 * 
	 * @param serverName
	 * @return
	 */
	public static DBServer use(String serverName) {
		return DBConfig.getDBServer(serverName);
	}

	/**
	 * to use the <em>'default'</em> DBServer.
	 * 
	 * @return
	 */
	public static DBServer usePrimaryDBServer() {
		return DBConfig.getPrimaryDBServer();
	}
}
