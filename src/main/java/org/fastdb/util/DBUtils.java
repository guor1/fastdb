package org.fastdb.util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.List;

import org.fastdb.internal.BeanDescriptor;
import org.fastdb.internal.BeanProperty;

public class DBUtils {
	public static <T> T build(BeanDescriptor<T> beanDescriptor, ResultSet rs) throws Exception {
		if (!rs.first() && !rs.next()) {
			return null;
		}
		Class<T> beanType = beanDescriptor.getBeanType();
		T instance = beanType.newInstance();

		List<BeanProperty> properties = beanDescriptor.getProperties();
		for (BeanProperty property : properties) {
			Method writeMethod = property.getWriteMethod();
			writeMethod.invoke(instance, rs.getObject(property.getDbColumn()));
		}
		return instance;
	}
}
