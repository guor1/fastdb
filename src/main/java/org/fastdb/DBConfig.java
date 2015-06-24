package org.fastdb;

import java.util.HashMap;
import java.util.Map;

import org.fastdb.bean.BeanDescriptor;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConfig {

	private Map<String, ComboPooledDataSource> dataSources = new HashMap<String, ComboPooledDataSource>();

	private static Map<String, BeanDescriptor<?>> beanMap = new HashMap<String, BeanDescriptor<?>>();

	private String primaryDataSourceName;

	public void configure() {
		SysProperties.getProperty("datasource.default");
	}

	public ComboPooledDataSource getDataSource(String dataSourceName) {
		return dataSources.get(dataSourceName);
	}

	public String getPrimaryDataSourceName() {
		return primaryDataSourceName;
	}

	public void setPrimaryDataSourceName(String primaryDataSourceName) {
		this.primaryDataSourceName = primaryDataSourceName;
	}

	public static <T> BeanDescriptor<T> getBeanDescriptor(Class<T> klass) {
		return getBeanDescriptor(klass, true);
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
