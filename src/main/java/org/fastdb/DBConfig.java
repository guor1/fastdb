package org.fastdb;

import java.util.HashMap;
import java.util.Map;

import org.fastdb.bean.BeanDescriptor;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConfig {

	private Map<String, ComboPooledDataSource> dataSources = new HashMap<String, ComboPooledDataSource>();

	private Map<String, BeanDescriptor<?>> beanMap = new HashMap<String, BeanDescriptor<?>>();

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

	@SuppressWarnings("unchecked")
	public <T> BeanDescriptor<T> getBeanDescriptor(Class<T> klass) {
		return (BeanDescriptor<T>) beanMap.get(klass.getName());
	}
}
