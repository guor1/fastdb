package org.fastdb.util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.fastdb.internal.BeanDescriptor;
import org.fastdb.internal.BeanProperty;

public class DBUtils {
	public static <T> T build(BeanDescriptor<T> beanDescriptor, ResultSet rs) throws Exception {
		if (!rs.first() && !rs.next()) {
			return null;
		}
		Class<T> beanType = beanDescriptor.getBeanType();
		T instance = beanType.newInstance();

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			String columnLabel = rsmd.getColumnLabel(i);
			BeanProperty beanProperty = beanDescriptor.getBeanProperty(columnLabel);
			Method writeMethod = beanProperty.getWriteMethod();
			writeMethod.invoke(instance, rs.getObject(columnLabel));
		}
		return instance;
	}
}
