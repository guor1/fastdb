package com.github.guor1.fastdb.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.sql.Types;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.github.guor1.fastdb.DBConfig;
import com.github.guor1.fastdb.FastdbException;
import com.github.guor1.fastdb.util.ReflectionUtils;

/**
 * 实体属性与数据库字段的映射
 * 
 * @author guor
 *
 */
public class BeanProperty {

	/****************************** 数据库字段信息 *****************************/
	/**
	 * 是否ID字段
	 */
	private boolean id = false;

	/**
	 * 是否版本号字段
	 */
	private boolean version = false;

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
	 * 数据库字段类型
	 */
	private int dbType;

	/**
	 * 默认值
	 */
	private Object defaultValue;

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

	/****************************** 实体bean信息 *****************************/
	/**
	 * 是否继承字段
	 */
	private boolean inherited;

	/**
	 * 字段类型
	 */
	private Class<?> propertyType;

	/**
	 * 字段名称
	 */
	private String name;

	/**
	 * 字段对象
	 */
	private Field field;

	/**
	 * 字段读方法
	 */
	private Method readMethod;

	/**
	 * 字段写方法
	 */
	private Method writeMethod;

	private boolean isGeneratedValue;

	/**
	 * 字段是否参与持久化
	 */
	private boolean isTransient = true;

	/**
	 * the many side of a relation
	 * 
	 * @ManyToMany and @OneToMany
	 */
	private boolean manyAssoc = false;

	/**
	 * the one side of a relation
	 * 
	 * @ManyToOne and @OneToOne
	 */
	private boolean oneAssoc = false;

	private String referencedColumnName;

	private final BeanDescriptor<?> beanDescriptor;

	public BeanProperty(BeanDescriptor<?> beanDescriptor, Field field) {
		this.beanDescriptor = beanDescriptor;
		this.propertyType = field.getType();
		this.name = field.getName();

		this.id = ReflectionUtils.annotationed(field, Id.class);
		this.isGeneratedValue = this.id ? ReflectionUtils.annotationed(field, GeneratedValue.class) : false;
		this.version = ReflectionUtils.annotationed(field, Version.class);
		this.isTransient = !ReflectionUtils.annotationed(field, Transient.class);

		boolean column = ReflectionUtils.annotationed(field, Column.class);
		if (column) {
			Column dbcolumn = ReflectionUtils.getAnnotation(field, Column.class);
			this.nullable = dbcolumn.nullable();
			this.unique = dbcolumn.unique();
			this.dbColumn = dbcolumn.name();
			this.dbColumnDefn = dbcolumn.columnDefinition();
			this.dbInsertable = dbcolumn.insertable();
			this.dbUpdatable = dbcolumn.updatable();
			this.dbLength = dbcolumn.length();
			this.dbScale = dbcolumn.scale();
		}

		this.oneAssoc = (ReflectionUtils.annotationed(field, ManyToOne.class) || ReflectionUtils.annotationed(field, OneToOne.class));
		if (this.oneAssoc) {
			boolean j = ReflectionUtils.annotationed(field, JoinColumn.class);
			if (j) {
				JoinColumn joinColumn = ReflectionUtils.getAnnotation(field, JoinColumn.class);
				String referencedColumnName = joinColumn.referencedColumnName();
				if (referencedColumnName == null || referencedColumnName.isEmpty()) {
					referencedColumnName = getDefaultReferencedColumnName(this.propertyType);
				}
				this.referencedColumnName = referencedColumnName;
				this.nullable = joinColumn.nullable();
				this.unique = joinColumn.unique();
				this.dbColumn = joinColumn.name();
				this.dbColumnDefn = joinColumn.columnDefinition();
				this.dbInsertable = joinColumn.insertable();
				this.dbUpdatable = joinColumn.updatable();
			} else {
				this.referencedColumnName = getDefaultReferencedColumnName(this.propertyType);
			}
		}
		this.manyAssoc = (ReflectionUtils.annotationed(field, ManyToMany.class) || ReflectionUtils.annotationed(field, OneToMany.class));
		// TODO deal with manyAssoc
		if (this.dbColumn == null || this.dbColumn.isEmpty()) {
			this.dbColumn = this.name;
		}
	}

	private String getDefaultReferencedColumnName(Class<?> beanType) {
		String referencedColumnName = null;
		BeanProperty idProperty = DBConfig.getBeanDescriptor(beanType).getIdProperty();
		if (idProperty == null) {
			throw new FastdbException("");
		} else {
			referencedColumnName = idProperty.getDbColumn();
		}
		return referencedColumnName;
	}

	public boolean isGeneratedValue() {
		return isGeneratedValue;
	}

	public boolean isManyAssoc() {
		return this.manyAssoc;
	}

	public boolean isOneAssoc() {
		return oneAssoc;
	}

	public String getReferencedColumnName() {
		return referencedColumnName;
	}

	public BeanDescriptor<?> getBeanDescriptor() {
		return beanDescriptor;
	}

	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}

	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}

	/**
	 * Return the getter method.
	 */
	public Method getReadMethod() {
		return readMethod;
	}

	/**
	 * Return the setter method.
	 */
	public Method getWriteMethod() {
		return writeMethod;
	}

	/**
	 * Return true if this object is part of an inheritance hierarchy.
	 */
	public boolean isInherited() {
		return inherited;
	}

	private static Object[] NO_ARGS = new Object[0];

	/**
	 * Return the value of the property method.
	 * 
	 * if manyAssoc is true,
	 */
	public Object getValue(Object bean) {
		Object value = null;
		try {
			value = readMethod.invoke(bean, NO_ARGS);
		} catch (Exception ex) {
			String beanType = bean == null ? "null" : bean.getClass().getName();
			String msg = "get " + name + " on [" + getName() + "] type[" + beanType + "] threw error.";
			throw new RuntimeException(msg, ex);
		}

		if (value != null && this.isOneAssoc()) {
			try {
				value = DBConfig.getBeanDescriptor(value.getClass()).getBeanProperty(getReferencedColumnName()).getReadMethod()
						.invoke(value, NO_ARGS);
			} catch (Exception ex) {
				String beanType = value == null ? "null" : value.getClass().getName();
				String msg = "get " + name + " on [" + getName() + "] type[" + beanType + "] threw error.";
				throw new RuntimeException(msg, ex);
			}
		}
		if (value == null && this.isVersion()) {
			if (propertyType == java.util.Date.class || propertyType == java.sql.Date.class || propertyType == java.sql.Timestamp.class) {
				return new Timestamp(System.currentTimeMillis());
			}
			return System.currentTimeMillis();
		}
		return value;
	}

	/**
	 * Return the name of the property.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the DB max length (varchar) or precision (decimal).
	 */
	public int getDbLength() {
		return dbLength;
	}

	/**
	 * Return the DB scale for numeric columns.
	 */
	public int getDbScale() {
		return dbScale;
	}

	/**
	 * Return a specific column DDL definition if specified (otherwise null).
	 */
	public String getDbColumnDefn() {
		return dbColumnDefn;
	}

	/**
	 * Return the bean Field associated with this property.
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Return true if this property is mandatory.
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * Return true if the DB column should be unique.
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * Return true if this is a version column used for concurrency checking.
	 */
	public boolean isVersion() {
		return version;
	}

	public String getDeployProperty() {
		return dbColumn;
	}

	/**
	 * The database column name this is mapped to.
	 */
	public String getDbColumn() {
		return dbColumn;
	}

	/**
	 * Return the database jdbc data type this is mapped to.
	 */
	public int getDbType() {
		return dbType;
	}

	public boolean isLobType(int type) {
		switch (type) {
		case Types.CLOB:
			return true;
		case Types.BLOB:
			return true;
		case Types.LONGVARBINARY:
			return true;
		case Types.LONGVARCHAR:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Return true if this property should be included in an Insert.
	 */
	public boolean isDbInsertable() {
		return dbInsertable;
	}

	/**
	 * Return true if this property should be included in an Update.
	 */
	public boolean isDbUpdatable() {
		return dbUpdatable;
	}

	/**
	 * Return the property type.
	 */
	public Class<?> getPropertyType() {
		return propertyType;
	}

	/**
	 * Return true if this is included in the unique id.
	 */
	public boolean isId() {
		return id;
	}

	/**
	 * Return the default value.
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	public boolean isTransient() {
		return isTransient;
	}

	public String toString() {
		return name;
	}
}
