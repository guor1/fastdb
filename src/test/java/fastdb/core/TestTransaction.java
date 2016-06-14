package fastdb.core;

import java.sql.SQLException;

import org.fastdb.DB;
import org.fastdb.FastdbException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fastdb.core.bean.User;

public class TestTransaction {

	@Before
	public void init() {
		H2Database.initTable();
		User u = new User();
		u.setUsername("guor");
		DB.persist(u);
	}

	@Test
	public void testTxRollback() throws SQLException {
		int size = DB.findList(User.class).size();
		Assert.assertEquals(1, size);

		DB.beginTransaction();
		try {
			User u = new User();
			u.setUsername("guor1");
			DB.persist(u);
			DB.persist(u);
		} catch (FastdbException e) {
			DB.rollbackTransaction();
		}
		size = DB.findList(User.class).size();
		Assert.assertEquals(1, size);
	}

	@Test
	public void testTxCommit() throws SQLException {
		int size = DB.findList(User.class).size();
		Assert.assertEquals(1, size);

		DB.beginTransaction();
		try {
			User u = new User();
			u.setUsername("guor1");
			DB.persist(u);
			u.setUsername("guor2");
			DB.persist(u);
		} catch (FastdbException e) {
			DB.rollbackTransaction();
		}
		DB.commitTransaction();
		size = DB.findList(User.class).size();
		Assert.assertEquals(3, size);
	}
}
