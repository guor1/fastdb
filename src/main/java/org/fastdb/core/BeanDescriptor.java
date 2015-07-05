package org.fastdb.core;

import java.lang.reflect.Field;
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

	/**
	 * 实体bean包含的属性，包括继承自父类的，全部属性
	 */
	private List<BeanProperty> properties = new LinkedList<BeanProperty>();

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
	}

	private void buildBeanProperties(Class<?> beanType) {
		try {
			Field[] fields = beanType.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (!Modifier.isStatic(field.getModifiers())) {
					this.properties.add(BeanPropertyBuilder.build(this, field));
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

	public List<BeanProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<BeanProperty> properties) {
		this.properties = properties;
	}

	public Class<T> getBeanType() {
		return beanType;
	}

	public String getTableName() {
		return tableName;
	}
}
