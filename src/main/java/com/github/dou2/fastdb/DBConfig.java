package com.github.dou2.fastdb;

import com.github.dou2.fastdb.bean.BeanDescriptor;
import com.github.dou2.fastdb.pool.DataSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DBConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBConfig.class);

    private static Map<String, DBServer> servers = new HashMap<>();

    private static Map<String, BeanDescriptor<?>> beanMap = new HashMap<>();

    static {
        configure();
    }

    private static DBServer primaryServer;

    private static void configure() {
        String primaryServerName = SysProperties.getProperty("fastdb.default");
        if (primaryServerName == null || primaryServerName.isEmpty()) {
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
        if (serverName == null || serverName.isEmpty()) {
            return getPrimaryDBServer();
        }
        DBServer dbServer = servers.get(serverName);
        if (dbServer != null) {
            return dbServer;
        }
        return getDBServerWithCreate(serverName);
    }

    public static DBServer createServer(String serverName, DataSource dataSource) {
        if (serverName == null || serverName.isEmpty()) {
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
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create(getServerProperty(serverName, "type"));
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
            beanDescriptor = new BeanDescriptor<>(klass);
            beanMap.put(klass.getName(), beanDescriptor);
        }
        return beanDescriptor;
    }
}
