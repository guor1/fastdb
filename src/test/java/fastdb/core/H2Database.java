package fastdb.core;

import java.beans.PropertyVetoException;

import org.fastdb.DB;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class H2Database {

	public static ComboPooledDataSource dataSource;

	static {
		dataSource = new ComboPooledDataSource(false);
		dataSource.setJdbcUrl("jdbc:h2:~/test");
		try {
			dataSource.setDriverClass("org.h2.Driver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		dataSource.setUser("root");
		dataSource.setPassword("root");
	}

	public static void initTable() {
		DB.createNativeQuery("DROP TABLE IF EXISTS `wdyq_contex`").executeUpdate();
		DB.createNativeQuery(
				"CREATE TABLE `wdyq_contex` (`id` int(10) NOT NULL AUTO_INCREMENT, `host` varchar(100) NOT NULL, `doctype` varchar(10) NULL, `template` varchar(255) NOT NULL, `expression` varchar(255) NOT NULL,`user` int(10) NOT NULL, `lastoptime` timestamp NULL, PRIMARY KEY (`id`), UNIQUE KEY `template_uk` (`template`) USING BTREE) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8")
				.executeUpdate();

		DB.createNativeQuery("DROP TABLE IF EXISTS `wdyq_user`").executeUpdate();
		DB.createNativeQuery(
				"CREATE TABLE `wdyq_user` (`id` int(10) NOT NULL AUTO_INCREMENT, `username` varchar(100) NOT NULL, PRIMARY KEY (`id`), UNIQUE KEY `username_uk` (`username`) USING BTREE) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8")
				.executeUpdate();
	}
}
