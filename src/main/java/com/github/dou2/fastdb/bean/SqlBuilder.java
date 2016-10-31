package com.github.dou2.fastdb.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dou2.fastdb.DBConfig;

public class SqlBuilder<T> {

	private static Logger LOGGER = LoggerFactory.getLogger(SqlBuilder.class);
	/**
	 * 
	 */
	// private String properties;
	private String findByIdSql;
	private String deleteByIdSql;
	private String insertSql;
	private final BeanDescriptor<T> beanDescriptor;

	public SqlBuilder(BeanDescriptor<T> beanDescriptor) {
		this.beanDescriptor = beanDescriptor;
	}

	public SqlBuilder(Class<T> beanType) {
		this.beanDescriptor = DBConfig.getBeanDescriptor(beanType);
	}

	public String buildFindByIdSql() {
		if (findByIdSql == null || findByIdSql.isEmpty()) {
			if (beanDescriptor.getIdProperty() == null) {
				LOGGER.warn("buildfindByIdSql fail for there is no ID property.");
				return null;
			}
			StringBuilder buf = new StringBuilder(" SELECT * FROM ");
			buf.append(beanDescriptor.getTableName()).append(" WHERE ");
			buf.append(beanDescriptor.getIdProperty().getDbColumn()).append("=?");
			findByIdSql = buf.toString();
		}
		return findByIdSql;
	}

	public String buildDeleteByIdSql() {
		if (this.deleteByIdSql != null) {
			return this.deleteByIdSql;
		}
		if (this.beanDescriptor.getIdProperty() == null) {
			LOGGER.warn("buildDeleteByIdSql fail for there is no ID property.");
			return null;
		}
		StringBuilder buf = new StringBuilder(" DELETE FROM ");
		buf.append(this.beanDescriptor.getTableName()).append(" WHERE ");
		buf.append(this.beanDescriptor.getIdProperty().getDbColumn()).append("=?");
		this.deleteByIdSql = buf.toString();
		return this.deleteByIdSql;
	}

	public String buildInsertSql() {
		if (this.insertSql != null) {
			return this.insertSql;
		}
		synchronized (SqlBuilder.class) {
			StringBuilder temp = new StringBuilder();

			StringBuilder buf = new StringBuilder(" INSERT INTO ");
			buf.append(this.beanDescriptor.getTableName()).append(" (");
			int count = 0;
			for (BeanProperty property : this.beanDescriptor.getProperties()) {
				if (!property.isDbInsertable() || property.isManyAssoc() || !property.isTransient()) {
					continue;
				}
				/**
				 * 自增ID
				 */
				if (property.isId() && property.isGeneratedValue()) {
					continue;
				}
				if (count++ > 0) {
					buf.append(", ");
					temp.append(",");
				}
				buf.append(property.getDbColumn());
				temp.append("?");
			}
			buf.append(") values (").append(temp).append(")");
			this.insertSql = buf.toString();
		}
		return this.insertSql;
	}
}
