package fastdb.core;

import java.sql.Timestamp;
import java.util.List;

import org.fastdb.DB;
import org.fastdb.DBRow;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fastdb.core.bean.Contex;
import fastdb.core.bean.User;

public class TestContex {

	@BeforeClass
	public static void init() {
		H2Database.initTable();
	}

	@Test
	public void testPersist() {
		User u = new User();
		u.setUsername("guor");
		DB.persist(u);

		DBRow dbRow = DB.createNativeQuery("SELECT * FROM `wdyq_user` WHERE `username`=?").setParameter(1, "guor").getSingleResult();
		Long id = Long.parseLong(dbRow.get("ID").toString());

		Contex contex = new Contex();
		contex.setHost("legal.firefox.news.cn");
		contex.setDoctype("html");
		contex.setExpression("c:article_content");
		contex.setTemplate("legal.firefox.news.cn/[d]/[d]/[d]/[*].jsp");
		contex.setUser(new User(id));
		contex.setLastoptime(new Timestamp(System.currentTimeMillis()));
		DB.persist(contex);
	}

	@Test
	public void testFindList() {
		List<Contex> list = DB.findList(Contex.class);
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void testFindById() {
		Contex contex = DB.find(Contex.class, 1);
		Assert.assertEquals("legal.firefox.news.cn", contex.getHost());
	}

	@Test
	public void testLazyLoad() {
		Contex contex = DB.find(Contex.class, 1);
		Assert.assertEquals("guor", contex.getUser().getUsername());
	}
}
