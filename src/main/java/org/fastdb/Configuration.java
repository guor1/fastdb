package org.fastdb;

import java.util.HashMap;
import java.util.Map;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Configuration {

	private Map<String, ComboPooledDataSource> dataSources = new HashMap<String, ComboPooledDataSource>();

	private String primaryDataSourceName;

	public void configure() {
		SysProperties.getProperty("datasource.default");
	}

	public Map<String, ComboPooledDataSource> getDataSources() {
		return dataSources;
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
}
