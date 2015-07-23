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

	private static DBServer primaryServer;

	private static void configure() {
		String primaryServerName = SysProperties.getProperty("fastdb.default");
		if (primaryServerName == null || primaryServerName.isEmpty()) {
			throw new FastdbException("The default DataSource has not been defined.");
		}
		primaryServer = getDBServerWithCreate(primaryServerName);
	}

	public static String getServerProperty(String serverName, String propName) {
		return "fastdb." + serverName + "." + propName;
	}

	public static DBServer getPrimaryDBServer() {
		return primaryServer;
	}

	public static DBServer getDBServer(String serverName) {
		if (serverName == null || serverName.isEmpty()) {
			return getPrimaryDBServer();
		}
		DBServer dbServer = servers.get(serverName);
		if (dbServer != null) {
			return dbServer;
		}
		return getDBServerWithCreate(serverName);
	}

	public static synchronized DBServer getDBServerWithCreate(String serverName) {
		DBServer dbServer = servers.get(serverName);
		if (dbServer == null) {
			ComboPooledDataSource dataSource = new ComboPooledDataSource(false);
			try {
				dataSource.setDriverClass(SysProperties.getProperty(getServerProperty(serverName, "driverClass")));
			} catch (PropertyVetoException e) {
				throw new FastdbException(e);
			}
			dataSource.setJdbcUrl(SysProperties.getProperty(getServerProperty(serverName, "jdbcUrl")));
			dataSource.setUser(SysProperties.getProperty(getServerProperty(serverName, "user")));
			dataSource.setPassword(SysProperties.getProperty(getServerProperty(serverName, "password")));
			dataSource.setCheckoutTimeout(SysProperties.getInt(getServerProperty(serverName, "checkoutTimeout"), 5000));
			dataSource.setMaxPoolSize(SysProperties.getInt(getServerProperty(serverName, "maxPoolSize"), 10));
			dataSource.setInitialPoolSize(SysProperties.getInt(getServerProperty(serverName, "initialPoolSize"), 1));
			dataSource.setMinPoolSize(SysProperties.getInt(getServerProperty(serverName, "minPoolSize"), 1));
			dataSource.setMaxIdleTime(SysProperties.getInt(getServerProperty(serverName, "maxIdleTime"), 30));
			dataSource.setAcquireIncrement(SysProperties.getInt(getServerProperty(serverName, "acquireIncrement"), 1));
			dataSource.setIdleConnectionTestPeriod(SysProperties.getInt(getServerProperty(serverName, "idleConnectionTestPeriod"), 30));
			dataSource.setTestConnectionOnCheckin(SysProperties.getBoolean(getServerProperty(serverName, "testConnectionOnCheckin"), true));
			dbServer = new DBServer(dataSource, serverName);
			servers.put(serverName, dbServer);
		}
		return dbServer;
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
