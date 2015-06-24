package fastdb.core;

import java.beans.PropertyVetoException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class H2Database {

	public static final ComboPooledDataSource dataSource;

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
}
