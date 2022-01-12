package com.github.guor1.fastdb.pool.impl;

import com.github.guor1.fastdb.pool.DataSourceBuilder;

import javax.sql.DataSource;

public class TomcatPool extends DataSourceBuilder {
    public static final String TYPE = org.apache.tomcat.jdbc.pool.DataSource.class.getName();

    @Override
    public DataSource build() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUsername(getProp("username"));
        dataSource.setPassword(getProp("password"));
        dataSource.setUrl(getProp("jdbcUrl"));
        dataSource.setDriverClassName(getProp("driverClassName"));
        return dataSource;
    }
}
