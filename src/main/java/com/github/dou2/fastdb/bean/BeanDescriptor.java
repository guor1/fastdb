package com.github.dou2.fastdb.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.github.dou2.fastdb.FastdbException;
import com.github.dou2.fastdb.util.ReflectionUtils;

public class BeanDescriptor<T> {

	/**
	 * 实体类名
	 */
	private final Class<T> beanType;

	/**
	 * 数据库表名
	 */
	private final String tableName;

	/**
	 * 实体bean包含的属性，包括继承自父类的，全部属性
	 */
	private List<BeanProperty> properties = new LinkedList<BeanProperty>();

	/**
	 * column --> BeanProperty
	 */
	private Map<String, BeanProperty> columnMap = new HashMap<String, BeanProperty>();

	private BeanProperty idProperty;

	private SqlBuilder<T> sqlBuilder;

	public BeanDescriptor(Class<T> beanType) {
		super();
		this.beanType = beanType;

		boolean entity = ReflectionUtils.annotationed(beanType, Entity.class);
		boolean table = ReflectionUtils.annotationed(beanType, Table.class);
		if (!entity || !table) {
			throw new FastdbException("非实体类对象，无法提取实体信息。");
		}
		this.tableName = ReflectionUtils.getAnnotation(beanType, Table.class).name();
		this.buildBeanProperties(beanType);
		this.sqlBuilder = new SqlBuilder<T>(this);
	}

	private void buildBeanProperties(Class<?> beanType) {
		try {
			Method[] declaredMethods = beanType.getDeclaredMethods();
			Field[] fields = beanType.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (!Modifier.isStatic(field.getModifiers())) {
					BeanProperty beanProperty = new BeanProperty(this, field);
					beanProperty.setReadMethod(ReflectionUtils.findGetter(field, declaredMethods));
					beanProperty.setWriteMethod(ReflectionUtils.findSetter(field, declaredMethods));

					this.properties.add(beanProperty);
					this.columnMap.put(beanProperty.getDbColumn().toUpperCase(), beanProperty);

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

	public Class<T> getBeanType() {
		return beanType;
	}

	public String getTableName() {
		return tableName;
	}

	public String buildFindByIdSql() {
		return sqlBuilder.buildFindByIdSql();
	}

	public String buildDeleteByIdSql() {
		return sqlBuilder.buildDeleteByIdSql();
	}

	public String buildInsertSql() {
		return sqlBuilder.buildInsertSql();
	}

	public List<BeanProperty> getProperties() {
		return properties;
	}

	public BeanProperty getBeanProperty(String columnName) {
		return columnMap.get(columnName.toUpperCase());
	}

	public Object getValue(Object bean, String property) {
		return getBeanProperty(property).getValue(bean);
	}

	public BeanProperty getIdProperty() {
		return idProperty;
	}

	public Object getIdValue(Object bean) {
		return idProperty.getValue(bean);
	}

	public String getUpdateSql() {
		return null;
	}

	@Override
	public String toString() {
		return "BeanDescriptor [beanType=" + getBeanType() + ", tableName=" + getTableName() + "]";
	}
}
