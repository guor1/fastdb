package com.github.dou2.fastdb.pool.impl;

import com.github.dou2.fastdb.pool.DataSourceBuilder;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

public class C3p0Pool extends DataSourceBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(C3p0Pool.class);
    public static final String TYPE = ComboPooledDataSource.class.getName();

    @Override
    public DataSource build() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(getProp("driverClassName"));
        } catch (PropertyVetoException e) {
            LOGGER.error(e.getMessage(), e);
        }
        dataSource.setJdbcUrl(getProp("jdbcUrl"));
        dataSource.setUser(getProp("username"));
        dataSource.setPassword(getProp("password"));
        return dataSource;
    }


}
