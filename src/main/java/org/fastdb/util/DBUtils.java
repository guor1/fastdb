package org.fastdb.util;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.fastdb.DBRow;
import org.fastdb.DBServer;
import org.fastdb.bean.BeanDescriptor;
import org.fastdb.bean.BeanProperty;
import org.fastdb.bean.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtils {
	private static Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);

	public static <T> T buildResult(DBServer dbServer, BeanDescriptor<T> beanDescriptor, ResultSet rs) throws Exception {
		Class<T> beanType = beanDescriptor.getBeanType();
		T instance = beanType.newInstance();

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnLabel = rsmd.getColumnLabel(i);
			BeanProperty beanProperty = beanDescriptor.getBeanProperty(columnLabel);
			if (beanProperty == null) {
				LOGGER.debug("Unmapped column " + columnLabel + " in " + beanDescriptor.getBeanType());
				continue;
			}
			Method writeMethod = beanProperty.getWriteMethod();
			/**
			 * lazy load the many side object
			 */
			if (beanProperty.isManyAssoc()) {
				writeMethod.invoke(instance, LazyInitializer.load(dbServer, beanProperty.getPropertyType(), rs.getObject(columnLabel)));
			} else {
				writeMethod.invoke(instance, rs.getObject(columnLabel));
			}
		}
		return instance;
	}

	public static <T> List<T> buildResult(DBServer dbServer, BeanDescriptor<T> beanDescriptor, List<DBRow> dbRows) throws Exception {
		List<T> result = new LinkedList<T>();
		if (dbRows == null || dbRows.isEmpty()) {
			return result;
		}
		for (DBRow row : dbRows) {
			result.add(buildResult(dbServer, beanDescriptor, row));
		}
		return result;
	}

	public static <T> T buildResult(DBServer dbServer, BeanDescriptor<T> beanDescriptor, DBRow dbRow) throws Exception {
		if (dbRow == null) {
			return null;
		}
		Class<T> beanType = beanDescriptor.getBeanType();
		T instance = beanType.newInstance();

		Iterator<String> iterator = dbRow.keySet().iterator();
		while (iterator.hasNext()) {
			String columnLabel = iterator.next();
			BeanProperty beanProperty = beanDescriptor.getBeanProperty(columnLabel);
			if (beanProperty == null) {
				LOGGER.debug("Unmapped column " + columnLabel + " in " + beanDescriptor.getBeanType());
				continue;
			}
			Method writeMethod = beanProperty.getWriteMethod();
			/**
			 * lazy load the many side object
			 */
			if (beanProperty.isOneAssoc()) {
				writeMethod.invoke(instance, LazyInitializer.load(dbServer, beanProperty.getPropertyType(), dbRow.get(columnLabel)));
			} else {
				writeMethod.invoke(instance, dbRow.get(columnLabel));
			}
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

	public static void setParameterValue(PreparedStatement ps, int paramIndex, int sqlType, Object inValue) throws SQLException {
		if (sqlType == Types.VARCHAR || sqlType == Types.LONGVARCHAR || (sqlType == Types.CLOB && isStringValue(inValue.getClass()))) {
			ps.setString(paramIndex, inValue.toString());
		} else if (sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) {
			if (inValue instanceof BigDecimal) {
				ps.setBigDecimal(paramIndex, (BigDecimal) inValue);
			} else {
				ps.setObject(paramIndex, inValue, sqlType);
			}
		} else if (sqlType == Types.DATE) {
			if (inValue instanceof java.util.Date) {
				if (inValue instanceof java.sql.Date) {
					ps.setDate(paramIndex, (java.sql.Date) inValue);
				} else {
					ps.setDate(paramIndex, new java.sql.Date(((java.util.Date) inValue).getTime()));
				}
			} else if (inValue instanceof Calendar) {
				Calendar cal = (Calendar) inValue;
				ps.setDate(paramIndex, new java.sql.Date(cal.getTime().getTime()), cal);
			} else {
				ps.setObject(paramIndex, inValue, Types.DATE);
			}
		} else if (sqlType == Types.TIME) {
			if (inValue instanceof java.util.Date) {
				if (inValue instanceof java.sql.Time) {
					ps.setTime(paramIndex, (java.sql.Time) inValue);
				} else {
					ps.setTime(paramIndex, new java.sql.Time(((java.util.Date) inValue).getTime()));
				}
			} else if (inValue instanceof Calendar) {
				Calendar cal = (Calendar) inValue;
				ps.setTime(paramIndex, new java.sql.Time(cal.getTime().getTime()), cal);
			} else {
				ps.setObject(paramIndex, inValue, Types.TIME);
			}
		} else if (sqlType == Types.TIMESTAMP) {
			if (inValue instanceof java.util.Date) {
				if (inValue instanceof java.sql.Timestamp) {
					ps.setTimestamp(paramIndex, (java.sql.Timestamp) inValue);
				} else {
					ps.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
				}
			} else if (inValue instanceof Calendar) {
				Calendar cal = (Calendar) inValue;
				ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()), cal);
			} else {
				ps.setObject(paramIndex, inValue, Types.TIMESTAMP);
			}
		} else {
			if (inValue == null) {
				ps.setObject(paramIndex, null);
				return;
			}
			if (isStringValue(inValue.getClass())) {
				ps.setString(paramIndex, inValue.toString());
			} else if (isDateValue(inValue.getClass())) {
				ps.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
			} else if (inValue instanceof Calendar) {
				Calendar cal = (Calendar) inValue;
				ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()), cal);
			} else {
				ps.setObject(paramIndex, inValue);
			}
		}
	}

	private static boolean isStringValue(Class<?> inValueType) {
		return (CharSequence.class.isAssignableFrom(inValueType) || StringWriter.class.isAssignableFrom(inValueType));
	}

	private static boolean isDateValue(Class<?> inValueType) {
		return (java.util.Date.class.isAssignableFrom(inValueType) && !(java.sql.Date.class.isAssignableFrom(inValueType)
				|| java.sql.Time.class.isAssignableFrom(inValueType) || java.sql.Timestamp.class.isAssignableFrom(inValueType)));
	}
}
