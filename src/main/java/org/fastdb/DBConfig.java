package org.fastdb;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import org.fastdb.bean.BeanDescriptor;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConfig {

	private static Map<String, DBServer> servers = new HashMap<String, DBServer>();

	private static Map<String, BeanDescriptor<?>> beanMap = new HashMap<String, BeanDescriptor<?>>();
	static {
		configure();
	}

	private static String primaryServerName;

	public static void configure() {
		String dataSourceName = SysProperties.getProperty("fastdb.default");
		if (dataSourceName == null || dataSourceName.isEmpty()) {
			throw new FastdbException("The default DataSource has not been defined.");
		}
		primaryServerName = dataSourceName;

		ComboPooledDataSource dataSource = new ComboPooledDataSource(false);
		try {
			dataSource.setDriverClass(SysProperties.getProperty(getServerProperty("driverClass")));
		} catch (PropertyVetoException e) {
			throw new FastdbException(e);
		}
		dataSource.setJdbcUrl(SysProperties.getProperty(getServerProperty("jdbcUrl")));
		dataSource.setUser(SysProperties.getProperty(getServerProperty("user")));
		dataSource.setPassword(SysProperties.getProperty(getServerProperty("password")));
		dataSource.setCheckoutTimeout(SysProperties.getInt(getServerProperty("checkoutTimeout"), 5000));
		dataSource.setMaxPoolSize(SysProperties.getInt(getServerProperty("maxPoolSize"), 10));
		dataSource.setInitialPoolSize(SysProperties.getInt(getServerProperty("initialPoolSize"), 1));
		dataSource.setMinPoolSize(SysProperties.getInt(getServerProperty("initialPoolSize"), 3));
		dataSource.setMaxIdleTime(SysProperties.getInt(getServerProperty("maxIdleTime"), 5000));
		dataSource.setAcquireIncrement(SysProperties.getInt(getServerProperty("acquireIncrement"), 1));
		dataSource.setTestConnectionOnCheckin(true);
		servers.put(primaryServerName, new DBServer(dataSource));
	}

	public static String getServerProperty(String propName) {
		return getServerProperty(primaryServerName, propName);
	}

	public static String getServerProperty(String serverName, String propName) {
		return "fastdb." + serverName + "." + propName;
	}

	public static DBServer getPrimaryDBServer() {
		return servers.get(primaryServerName);
	}

	public static <T> BeanDescriptor<T> getBeanDescriptor(Class<T> klass) {
		return getBeanDescriptorWithCreate(klass);
	}

	@SuppressWarnings("unchecked")
	public static <T> BeanDescriptor<T> getBeanDescriptor(Class<T> klass, boolean throwIfNotExists) {
		BeanDescriptor<T> beanDescriptor = (BeanDescriptor<T>) beanMap.get(klass.getName());
		if (beanDescriptor == null && throwIfNotExists) {
			throw new FastdbException("No BeanDescriptor assosiated with " + klass.getName());
		}
		return beanDescriptor;
	}

	public static <T> BeanDescriptor<T> getBeanDescriptorWithCreate(Class<T> klass) {
		BeanDescriptor<T> beanDescriptor = getBeanDescriptor(klass, false);
		if (beanDescriptor != null) {
			return beanDescriptor;
		}
		synchronized (DBConfig.class) {
			beanDescriptor = new BeanDescriptor<T>(klass);
			beanMap.put(klass.getName(), beanDescriptor);
		}
		return beanDescriptor;
	}
}
