package com.github.guor1.fastdb.pool.impl;

import com.github.guor1.fastdb.pool.DataSourceBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class HikariPool extends DataSourceBuilder {
    public static final String TYPE = HikariDataSource.class.getName();

    @Override
    public DataSource build() {
        HikariConfig config = new HikariConfig();
        config.setUsername(getProp("username"));
        config.setPassword(getProp("password"));
        config.setJdbcUrl(getProp("jdbcUrl"));
        config.setDriverClassName(getProp("driverClassName"));
        return new HikariDataSource(config);
    }
}
