package com.github.dou2.fastdb.pool.impl;

import com.github.dou2.fastdb.pool.DataSourceBuilder;

import javax.sql.DataSource;

public class TomcatPool extends DataSourceBuilder {
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
