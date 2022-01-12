package com.github.guor1.fastdb.pool;

import com.github.guor1.fastdb.FastdbException;
import com.github.dou2.fastdb.pool.impl.*;
import com.github.guor1.fastdb.pool.impl.*;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public abstract class DataSourceBuilder {
    private Map<String, String> properties = new HashMap<String, String>();

    public abstract DataSource build();

    public static DataSourceBuilder create(Class<DataSource> type) {
        return create(type.getName());
    }

    public static DataSourceBuilder create(String type) {
        if (StringUtils.isEmpty(type)) {
            throw new FastdbException("数据库连接池类型不能为空.");
        }
        if (StringUtils.equalsIgnoreCase(HikariPool.TYPE, type)) {
            return new HikariPool();
        } else if (StringUtils.equalsIgnoreCase(DbcpPool.TYPE, type)) {
            return new DbcpPool();
        } else if (StringUtils.equalsIgnoreCase(C3p0Pool.TYPE, type)) {
            return new C3p0Pool();
        } else if (StringUtils.equalsIgnoreCase(DruidPool.TYPE, type)) {
            return new DruidPool();
        } else if (StringUtils.equalsIgnoreCase(TomcatPool.TYPE, type)) {
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

