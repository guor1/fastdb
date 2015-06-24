package org.fastdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.fastdb.bean.BeanDescriptor;
import org.fastdb.internal.DBQueryImpl;
import org.fastdb.util.DBUtils;

public class DBServer {

	private final DataSource dataSource;

	private DBConfig dbConfig;

	public DBServer(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	public <T> T find(Class<T> entityClass, Object primaryKey) {
		BeanDescriptor<T> beanDescriptor = dbConfig.getBeanDescriptor(entityClass);
		try {
			Connection conn = dataSource.getConnection();
			try {
				PreparedStatement pstmt = conn.prepareStatement(beanDescriptor.getFindByIdSql());
				try {
					pstmt.setObject(1, primaryKey);
					ResultSet rs = pstmt.executeQuery();
					try {
						if (rs.next()) {
							return DBUtils.buildResult(beanDescriptor, rs);
						}
					} finally {
						rs.close();
					}
				} finally {
					pstmt.close();
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			throw new FastdbException(e);
		}
		return null;
	}

	public <T> void persist(T entity) {
		BeanDescriptor<?> beanDescriptor = dbConfig.getBeanDescriptor(entity.getClass());
		try {
			Connection conn = dataSource.getConnection();
			try {
				PreparedStatement pstmt = conn.prepareStatement(beanDescriptor.getInsertSql());
				try {
					// TODO persist
				} finally {
					pstmt.close();
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			throw new FastdbException(e);
		}
	}

	public <T> T merge(T entity) {
		BeanDescriptor<?> beanDescriptor = dbConfig.getBeanDescriptor(entity.getClass());
		try {
			Connection conn = dataSource.getConnection();
			try {
				PreparedStatement pstmt = conn.prepareStatement(beanDescriptor.getUpdateSql());
				try {
					// TODO merge
					return null;
				} finally {
					pstmt.close();
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			throw new FastdbException(e);
		}
	}

	public <T> void remove(T entity) {
		BeanDescriptor<?> beanDescriptor = dbConfig.getBeanDescriptor(entity.getClass());
		try {
			Connection conn = dataSource.getConnection();
			try {
				PreparedStatement pstmt = conn.prepareStatement(beanDescriptor.getDeleteByIdSql());
				try {
					pstmt.setObject(1, beanDescriptor.getIdValue(entity));
					pstmt.executeUpdate();
				} finally {
					pstmt.close();
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			throw new FastdbException(e);
		}
	}

	/**
	 * create a query based on a manual sql
	 * 
	 * @param sql sql to be executed
	 * @return
	 */
	public DBQuery createNativeQuery(String sqlString) {
		return new DBQueryImpl(this, sqlString);
	}
}
