package org.fastdb.util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.List;

import org.fastdb.DBRow;
import org.fastdb.bean.BeanDescriptor;
import org.fastdb.bean.BeanProperty;

public class DBUtils {
	public static <T> T buildResult(BeanDescriptor<T> beanDescriptor, ResultSet rs) throws Exception {
		Class<T> beanType = beanDescriptor.getBeanType();
		T instance = beanType.newInstance();

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnLabel = rsmd.getColumnLabel(i);
			BeanProperty beanProperty = beanDescriptor.getBeanProperty(columnLabel);
			Method writeMethod = beanProperty.getWriteMethod();
			writeMethod.invoke(instance, rs.getObject(columnLabel));
		}
		return instance;
	}

	public static List<DBRow> buildResult(ResultSet rs) throws Exception {
		List<DBRow> rows = new LinkedList<DBRow>();
		boolean first = rs.first();
		if (!first) {
			return rows;
		}
		while (first || rs.next()) {
			DBRow sqlRow = new DBRow();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnLabel = rsmd.getColumnLabel(i);
				sqlRow.put(columnLabel, rs.getObject(columnLabel));
			}
			rows.add(sqlRow);
			first = false;
		}
		return rows;
	}
}
