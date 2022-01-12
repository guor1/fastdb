package com.github.guor1.fastdb.core;

import com.github.guor1.fastdb.DB;
import com.github.guor1.fastdb.DBConfig;
import com.github.guor1.fastdb.SysProperties;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDBConfig {

    @Test
    public void testInit() {
        Assert.assertEquals("org.h2.Driver", SysProperties.getProperty(getServerProperty("driverClassName")));
        Assert.assertEquals("jdbc:h2:~/test", SysProperties.getProperty(getServerProperty("jdbcUrl")));
        Assert.assertEquals("root", SysProperties.getProperty(getServerProperty("user")));
        Assert.assertEquals("root", SysProperties.getProperty(getServerProperty("password")));
        Assert.assertEquals(5000, SysProperties.getInt(getServerProperty("checkoutTimeout"), 5000));
        Assert.assertEquals(15, SysProperties.getInt(getServerProperty("maxPoolSize"), 10));
        Assert.assertEquals(3, SysProperties.getInt(getServerProperty("initialPoolSize"), 1));
        Assert.assertEquals(3, SysProperties.getInt(getServerProperty("minPoolSize"), 3));
        Assert.assertEquals(60, SysProperties.getInt(getServerProperty("maxIdleTime"), 30));
        Assert.assertEquals(1, SysProperties.getInt(getServerProperty("acquireIncrement"), 1));
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = DBConfig.getPrimaryDBServer().getConnection();
        connection.close();

        Connection conn = DB.use("h2db").getConnection();
        conn.close();
    }

    @Test
    public void testDebugSql() {
        Assert.assertTrue(SysProperties.debugSql());
    }

    public static String getServerProperty(String propName) {
        return getServerProperty("h2db", propName);
    }

    public static String getServerProperty(String serverName, String propName) {
        return "fastdb." + serverName + "." + propName;
    }
}
