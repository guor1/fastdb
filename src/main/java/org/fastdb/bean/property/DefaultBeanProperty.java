package org.fastdb.bean.property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.fastdb.BeanProperty;
import org.fastdb.bean.BeanDescriptor;

public class DefaultBeanProperty implements BeanProperty {
	/**
	 * 字段对象
	 */
	private final Field field;

	private final BeanDescriptor<?> beanDescriptor;
	/**
	 * 字段名称
	 */
	private String name;

	/**
	 * 字段读方法
	 */
	private Method readMethod;

	/**
	 * 字段写方法
	 */
	private Method writeMethod;
	/**
	 * 字段类型
	 */
	private Class<?> propertyType;

	/**
	 * 是否可以为空
	 */
	private boolean nullable = true;

	/**
	 * 是否唯一
	 */
	private boolean unique = false;

	/**
	 * 数据库字段名
	 */
	private String dbColumn;

	/**
	 * 字段长度
	 */
	private int dbLength = 255;

	/**
	 * 字段精度
	 */
	private int dbScale = 0;

	/**
	 * 字段注释
	 */
	private String dbColumnDefn;

	/**
	 * 是否支持插入
	 */
	private boolean dbInsertable = true;

	/**
	 * 是否支持更新
	 */
	private boolean dbUpdatable = true;

	/**
	 * 字段是否参与持久化
	 */
	private boolean isTransient = true;

	public DefaultBeanProperty(BeanDescriptor<?> beanDescriptor, Field field) {
		this.beanDescriptor = beanDescriptor;
		this.field = field;
	}

	@Override
	public BeanDescriptor<?> getBeanDescriptor() {
		return this.beanDescriptor;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Field getField() {
		return this.field;
	}

	@Override
	public Method getReadMethod() {
		return this.readMethod;
	}

	@Override
	public Method getWriteMethod() {
		return this.writeMethod;
	}

	@Override
	public Object getValue(Object bean) {
		return null;
	}

	@Override
	public Class<?> getPropertyType() {
		return this.propertyType;
	}

	@Override
	public boolean isNullable() {
		return this.nullable;
	}

	@Override
	public boolean isUnique() {
		return this.unique;
	}

	@Override
	public int getDbLength() {
		return this.dbLength;
	}

	@Override
	public int getDbScale() {
		return this.dbScale;
	}

	@Override
	public String getDbColumn() {
		return this.dbColumn;
	}

	@Override
	public String getDbColumnDefn() {
		return this.dbColumnDefn;
	}

	@Override
	public boolean isDbInsertable() {
		return this.dbInsertable;
	}

	@Override
	public boolean isDbUpdatable() {
		return this.dbUpdatable;
	}

	@Override
	public boolean isTransient() {
		return this.isTransient;
	}
}
