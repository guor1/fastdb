package org.fastdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.fastdb.internal.BeanDescriptor;
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

	/**
	 * 根据主键，查找对象
	 * 
	 * @param entityClass
	 * @param primaryKey
	 * @return
	 */
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
						return DBUtils.build(beanDescriptor, rs);
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
}
