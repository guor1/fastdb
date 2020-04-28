package com.github.dou2.fastdb.pool.impl;

import com.github.dou2.fastdb.pool.DataSourceBuilder;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class DbcpPool extends DataSourceBuilder {
    public static final String TYPE = BasicDataSource.class.getName();

    @Override
    public DataSource build() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername(getProp("username"));
        dataSource.setPassword(getProp("password"));
        dataSource.setUrl(getProp("jdbcUrl"));
        dataSource.setDriverClassName(getProp("driverClassName"));
        return dataSource;
    }
}
