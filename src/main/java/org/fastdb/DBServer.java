package org.fastdb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.fastdb.bean.BeanDescriptor;
import org.fastdb.bean.BeanProperty;
import org.fastdb.internal.DBQueryImpl;
import org.fastdb.internal.TransactionImpl;
import org.fastdb.internal.TransactionThreadLocal;
import org.fastdb.util.DBUtils;

public class DBServer {

	private final DataSource dataSource;

	private final String serverName;

	public DBServer(DataSource dataSource, String serverName) {
		this.dataSource = dataSource;
		this.serverName = serverName;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	public String getServerName() {
		return serverName;
	}

	public void persist(Object entity) {
		BeanDescriptor<?> beanDescriptor = DBConfig.getBeanDescriptor(entity.getClass());
		DBQuery dbQuery = createNativeQuery(beanDescriptor.buildInsertSql());
		int p = 1;
		List<BeanProperty> properties = beanDescriptor.getProperties();
		for (BeanProperty property : properties) {
			if (!property.isDbInsertable() || property.isManyAssoc() || !property.isTransient()) {
				continue;
			}
			if (property.isId() && property.isGeneratedValue()) {
				continue;
			}
			dbQuery.setParameter(p++, property.getValue(entity));
		}
		dbQuery.executeUpdate();
	}

	public <T> List<T> findList(Class<T> entityClass) {
		BeanDescriptor<T> beanDescriptor = DBConfig.getBeanDescriptor(entityClass);
		try {
			return DBUtils.buildResult(this, beanDescriptor, createNativeQuery("SELECT * FROM " + beanDescriptor.getTableName()).findList());
		} catch (Exception e) {
			throw new FastdbException(e);
		}
	}

	/**
	 * find unique entity by primaryKey
	 * 
	 * @param entityClass
	 * @param primaryKey
	 * @return
	 */
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		BeanDescriptor<T> beanDescriptor = DBConfig.getBeanDescriptor(entityClass);
		try {
			return DBUtils.buildResult(this, beanDescriptor, createNativeQuery(beanDescriptor.buildFindByIdSql()).setParameter(1, primaryKey)
					.findUnique());
		} catch (Exception e) {
			throw new FastdbException(e);
		}
	}

	public <T> int delete(Class<T> beanType, Object primaryKey) {
		BeanDescriptor<T> beanDescriptor = DBConfig.getBeanDescriptor(beanType);
		return createNativeQuery(beanDescriptor.buildDeleteByIdSql()).setParameter(1, primaryKey).executeUpdate();
	}

	/**
	 * 
	 * @param entity
	 */
	public int delete(Object entity) {
		BeanDescriptor<?> beanDescriptor = DBConfig.getBeanDescriptor(entity.getClass());
		return delete(entity.getClass(), beanDescriptor.getIdValue(entity));
	}

	/**
	 * create a query based on a manual sql
	 * 
	 * @param sql
	 *            sql to be executed
	 * @return
	 */
	public DBQuery createNativeQuery(String sqlString) {
		return new DBQueryImpl(this, sqlString);
	}

	/**
	 * Start a new transaction putting it into a ThreadLocal.
	 * 
	 * @throws SQLException
	 */
	public Transaction beginTransaction() throws SQLException {
		TransactionImpl tx = new TransactionImpl(getConnection());
		TransactionThreadLocal.put(getServerName(), tx);
		return tx;
	}

	/**
	 * Returns the current transaction or null if there is no current
	 * transaction in scope.
	 */
	public Transaction currentTransaction() {
		return TransactionThreadLocal.get(getServerName());
	}

	/**
	 * Commit the current transaction.
	 */
	public void commitTransaction() {
		Transaction transaction = currentTransaction();
		if (transaction != null) {
			transaction.commit();
		}
	}

	/**
	 * Rollback the current transaction.
	 */
	public void rollbackTransaction() {
		Transaction transaction = currentTransaction();
		if (transaction != null) {
			transaction.rollback();
		}
	}
}
