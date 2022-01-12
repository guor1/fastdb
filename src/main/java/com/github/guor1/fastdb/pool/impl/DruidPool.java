package com.github.guor1.fastdb.pool.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.guor1.fastdb.pool.DataSourceBuilder;

import javax.sql.DataSource;

public class DruidPool extends DataSourceBuilder {
    public static final String TYPE = DruidDataSource.class.getName();

    @Override
    public DataSource build() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(getProp("username"));
        dataSource.setPassword(getProp("password"));
        dataSource.setUrl(getProp("jdbcUrl"));
        dataSource.setDriverClassName(getProp("driverClassName"));
        return dataSource;
    }
}
