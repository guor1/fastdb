package com.github.dou2.fastdb.pool;

import com.github.dou2.fastdb.FastdbException;
import com.github.dou2.fastdb.pool.impl.*;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public abstract class DataSourceBuilder {
    private Map<String, String> properties = new HashMap<>();

    public abstract DataSource build();

    public static DataSourceBuilder create(Class<DataSource> type) {
        return create(type.getName());
    }

    public static DataSourceBuilder create(String type) {
        if (StringUtils.isEmpty(type)) {
            throw new FastdbException("数据库连接池类型不能为空.");
        }
        if (StringUtils.equalsIgnoreCase("com.zaxxer.hikari.HikariDataSource", type)) {
            return new HikariPool();
        } else if (StringUtils.equalsIgnoreCase("org.apache.commons.dbcp2.BasicDataSource", type)) {
            return new DbcpPool();
        } else if (StringUtils.equalsIgnoreCase("com.mchange.v2.c3p0.ComboPooledDataSource", type)) {
            return new C3p0Pool();
        } else if (StringUtils.equalsIgnoreCase("com.alibaba.druid.pool.DruidDataSource", type)) {
            return new DruidPool();
        } else if (StringUtils.equalsIgnoreCase("org.apache.tomcat.jdbc.pool.DataSource", type)) {
            return new TomcatPool();
        }
        throw new FastdbException("未知的数据库连接池类型.");
    }

    public DataSourceBuilder jdbcUrl(String jdbcUrl) {
        this.properties.put("jdbcUrl", jdbcUrl);
        return this;
    }

    public DataSourceBuilder driverClassName(String driverClassName) {
        this.properties.put("driverClassName", driverClassName);
        return this;
    }

    public DataSourceBuilder username(String username) {
        this.properties.put("username", username);
        return this;
    }

    public DataSourceBuilder password(String password) {
        this.properties.put("password", password);
        return this;
    }

    protected String getProp(String k) {
        return properties.get(k);
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}

