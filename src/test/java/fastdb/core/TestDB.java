package fastdb.core;

import java.util.List;

import org.fastdb.DB;
import org.fastdb.DBRow;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fastdb.core.bean.Contex;

public class TestDB {

	@Before
	public void init() {
		DB.createNativeQuery("delete from `wdyq_contex`").executeUpdate();

		Contex contex = new Contex();
		contex.setHost("legal.firefox.news.cn");
		contex.setDoctype("html");
		contex.setTemplate("legal.firefox.news.cn/[d]/[d]/[d]/[*].html");
		contex.setExpression("c:article_content");
		DB.persist(contex);
	}

	@Test
	public void test() {
		List<Contex> list = DB.findList(Contex.class);
		Assert.assertEquals(1, list.size());

		DBRow dbRow = DB.createNativeQuery("select * from `wdyq_contex` where `template`=?")
				.setParameter(1, "legal.firefox.news.cn/[d]/[d]/[d]/[*].html").getSingleResult();

		int id = Integer.parseInt(dbRow.get("ID").toString());
		Contex contex = DB.find(Contex.class, id);
		Assert.assertNotNull(contex);
		Assert.assertEquals("legal.firefox.news.cn", contex.getHost());
	}
}
