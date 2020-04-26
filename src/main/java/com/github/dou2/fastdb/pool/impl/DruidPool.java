package com.github.dou2.fastdb.pool.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.dou2.fastdb.pool.DataSourceBuilder;

import javax.sql.DataSource;

public class DruidPool extends DataSourceBuilder {
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
