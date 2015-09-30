package org.fastdb.internal;

import java.io.Serializable;

public class TypedValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2460488708353532061L;

	private final int sqlType;
	private final Object value;

	public TypedValue(int sqlType, Object value) {
		super();
		this.sqlType = sqlType;
		this.value = value;
	}

	public int getSqlType() {
		return sqlType;
	}

	public Object getValue() {
		return value;
	}
}
