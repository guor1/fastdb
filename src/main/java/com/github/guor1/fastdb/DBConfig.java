package com.github.guor1.fastdb;

import com.github.guor1.fastdb.bean.BeanDescriptor;
import com.github.guor1.fastdb.pool.DataSourceBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DBConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBConfig.class);

    private static Map<String, DBServer> servers = new HashMap<String, DBServer>();

    private static Map<String, BeanDescriptor<?>> beanMap = new HashMap<String, BeanDescriptor<?>>();

    static {
        configure();
    }

    private static DBServer primaryServer;

    private static void configure() {
        String primaryServerName = SysProperties.getProperty("fastdb.default");
        if (StringUtils.isEmpty(primaryServerName)) {
            throw new FastdbException("The default DataSource has not been defined.");
        }
        primaryServer = getDBServerWithCreate(primaryServerName);
    }

    public static String getServerProperty(String serverName, String propName) {
        return "fastdb." + serverName + "." + propName;
    }

    public static DBServer getPrimaryDBServer() {
        return primaryServer;
    }

    public static DBServer getDBServer(String serverName) {
        if (StringUtils.isEmpty(serverName)) {
            return getPrimaryDBServer();
        }
        DBServer dbServer = servers.get(serverName);
        if (dbServer != null) {
            return dbServer;
        }
        return getDBServerWithCreate(serverName);
    }

    public static DBServer createServer(String serverName, DataSource dataSource) {
        if (StringUtils.isEmpty(serverName)) {
            throw new FastdbException("DBServer name must not null.");
        }
        if (servers.containsKey(serverName)) {
            throw new FastdbException("DBServer with name=" + serverName + " already exists.");
        }
        DBServer dbServer = new DBServer(dataSource, serverName);
        servers.put(serverName, dbServer);
        return dbServer;
    }


    public static synchronized DBServer getDBServerWithCreate(String serverName) {
        DBServer dbServer = servers.get(serverName);
        if (dbServer == null) {
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create(SysProperties.getProperty(getServerProperty(serverName, "type")));
            dataSourceBuilder.username(SysProperties.getProperty(getServerProperty(serverName, "username")));
            dataSourceBuilder.password(SysProperties.getProperty(getServerProperty(serverName, "password")));
            dataSourceBuilder.jdbcUrl(SysProperties.getProperty(getServerProperty(serverName, "jdbcUrl")));
            dataSourceBuilder.driverClassName(SysProperties.getProperty(getServerProperty(serverName, "driverClassName")));
            dbServer = new DBServer(dataSourceBuilder.build(), serverName);
            servers.put(serverName, dbServer);
        }
        return dbServer;
    }

    public static <T> BeanDescriptor<T> getBeanDescriptor(Class<T> klass) {
        return getBeanDescriptorWithCreate(klass);
    }

    @SuppressWarnings("unchecked")
    public static <T> BeanDescriptor<T> getBeanDescriptor(Class<T> klass, boolean throwIfNotExists) {
        BeanDescriptor<T> beanDescriptor = (BeanDescriptor<T>) beanMap.get(klass.getName());
        if (beanDescriptor == null && throwIfNotExists) {
            throw new FastdbException("No BeanDescriptor assosiated with " + klass.getName());
        }
        return beanDescriptor;
    }

    public static <T> BeanDescriptor<T> getBeanDescriptorWithCreate(Class<T> klass) {
        BeanDescriptor<T> beanDescriptor = getBeanDescriptor(klass, false);
        if (beanDescriptor != null) {
            return beanDescriptor;
        }
        synchronized (DBConfig.class) {
            beanDescriptor = new BeanDescriptor<T>(klass);
            beanMap.put(klass.getName(), beanDescriptor);
        }
        return beanDescriptor;
    }
}
