package org.fastdb.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.fastdb.FastdbException;
import org.fastdb.util.ReflectionUtils;

public class BeanDescriptor<T> {
	/**
	 * 实体类名
	 */
	private final Class<T> beanType;

	/**
	 * 数据库表名
	 */
	private final String tableName;

	private final String tableAlias;

	/**
	 * 实体bean包含的属性，包括继承自父类的
	 */
	private List<BeanProperty> properties = new LinkedList<BeanProperty>();

	private BeanProperty idProperty;

	private String deleteByIdSql;

	private String findByIdSql;

	private String insertSql;

	public BeanDescriptor(Class<T> beanType) {
		super();
		this.beanType = beanType;

		boolean entity = ReflectionUtils.annotationed(beanType, Entity.class);
		boolean table = ReflectionUtils.annotationed(beanType, Table.class);
		if (!entity || !table) {
			throw new FastdbException("非实体类对象，无法提取实体信息。");
		}
		this.tableName = ReflectionUtils.getAnnotation(beanType, Table.class).name();
		this.tableAlias = "t0";
		this.buildBeanProperties(beanType);

		this.findByIdSql = buildFindByIdSql();
		this.deleteByIdSql = buildDeleteByIdSql();
		this.insertSql = buildInsertSql();
	}

	private void buildBeanProperties(Class<?> beanType) {
		try {
			Method[] declaredMethods = beanType.getDeclaredMethods();
			Field[] fields = beanType.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (!Modifier.isStatic(field.getModifiers())) {
					BeanProperty beanProperty = new BeanProperty(field, ReflectionUtils.findGetter(field, declaredMethods),
							ReflectionUtils.findSetter(field, declaredMethods));
					this.properties.add(beanProperty);
					if (beanProperty.isId()) {
						this.idProperty = beanProperty;
					}
				}
			}
			Class<?> superClass = beanType.getSuperclass();
			/**
			 * 计算父类直到Object的属性信息
			 */
			if (!superClass.equals(Object.class)) {
				buildBeanProperties(superClass);
			}
		} catch (Exception ex) {
			throw new FastdbException(ex);
		}
	}

	/**
	 * 编译根据ID查找记录SQL
	 * 
	 * @return
	 */
	private String buildFindByIdSql() {
		if (this.idProperty == null) {
			throw new FastdbException("没有找到ID属性，无法编译SQL.");
		}
		StringBuilder buf = new StringBuilder(" select * from ");
		buf.append(this.getTableName()).append(" where ");
		buf.append(idProperty.getDbColumn()).append("=?");
		return buf.toString();
	}

	private String buildDeleteByIdSql() {
		if (this.idProperty == null) {
			throw new FastdbException("没有找到ID属性，无法编译SQL.");
		}
		StringBuilder buf = new StringBuilder(" delete from ");
		buf.append(this.getTableName()).append(" where ");
		buf.append(idProperty.getDbColumn()).append("=?");
		return buf.toString();
	}

	private String buildInsertSql() {
		StringBuilder temp = new StringBuilder();
		StringBuilder buf = new StringBuilder(" insert into ");
		buf.append(this.getTableName()).append(" (");
		int count = 0;
		for (BeanProperty property : properties) {
			if (property.isId()) {
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
		return buf.toString();
	}

	public Class<T> getBeanType() {
		return beanType;
	}

	public String getTableName() {
		return tableName;
	}

	public String getTableAlias() {
		return tableAlias;
	}

	public String getFindByIdSql() {
		return findByIdSql;
	}

	public String getDeleteByIdSql() {
		return deleteByIdSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public List<BeanProperty> getProperties() {
		return properties;
	}

	public BeanProperty getBeanProperty(String propName) {
		return null;
	}

	public Object getValue(Object bean, String property) {
		return getBeanProperty(property).getValue(bean);
	}

	public Object getIdValue(Object bean) {
		return idProperty.getValue(bean);
	}

	public String getUpdateSql() {
		return null;
	}

	@Override
	public String toString() {
		return "BeanDescriptor [beanType=" + beanType + ", tableName=" + tableName + ", tableAlias=" + tableAlias + "]";
	}
}
