package fastdb.core;

import java.sql.Timestamp;
import java.util.List;

import org.fastdb.DBConfig;
import org.fastdb.DBQuery;
import org.fastdb.DBRow;
import org.fastdb.DBServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fastdb.core.bean.Contex;

public class TestDBServer {

	private DBServer dbServer;

	@Before
	public void init() {
		dbServer = new DBServer(H2Database.dataSource);
		DBConfig.getBeanDescriptorWithCreate(Contex.class);
		// String sql =
		// "CREATE TABLE `wdyq_contex` (`id` int(10) NOT NULL AUTO_INCREMENT, `host` varchar(100) NOT NULL, `doctype` varchar(10) NULL, `template` varchar(255) NOT NULL, `expression` varchar(255) NOT NULL, `lastoptime` timestamp NULL, PRIMARY KEY (`id`), UNIQUE KEY `template_uk` (`template`) USING BTREE) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
		// dbServer.createNativeQuery("DROP TABLE IF EXISTS `wdyq_contex`").executeUpdate();
		// int d = dbServer.createNativeQuery(sql).executeUpdate();
		// Assert.assertEquals(0, d);
	}

	@Test
	public void testNativeQuery() {
		DBQuery dbQuery = dbServer.createNativeQuery("select * from wdyq_contex");
		List<DBRow> resultList = dbQuery.getResultList();
		Assert.assertEquals(0, resultList.size());

		dbQuery = dbServer.createNativeQuery("insert into `wdyq_contex` (`host`,`doctype`,`template`,`expression`,`lastoptime`) values (?,?,?,?,?)");
		int p = 1;
		dbQuery.setParameter(p++, "legal.firefox.news.cn");
		dbQuery.setParameter(p++, "html");
		dbQuery.setParameter(p++, "legal.firefox.news.cn/[d]/[d]/[d]/[*].html");
		dbQuery.setParameter(p++, "c:article_content");
		dbQuery.setParameter(p++, new Timestamp(System.currentTimeMillis()));
		int d = dbQuery.executeUpdate();
		Assert.assertEquals(1, d);

		dbQuery = dbServer.createNativeQuery("select * from `wdyq_contex`");
		resultList = dbQuery.getResultList();
		Assert.assertEquals(1, resultList.size());
	}

	@Test
	public void testFind() {
		Contex contex = dbServer.find(Contex.class, 34);
		Assert.assertNotNull(contex);
		Assert.assertEquals("legal.firefox.news.cn", contex.getHost());
	}

	@Test
	public void testFindList() {
		List<Contex> resultList = dbServer.findList(Contex.class);
		Assert.assertEquals(2, resultList.size());
	}

	@Test
	public void testDelete() {
		int d = dbServer.delete(Contex.class, 34);
		Assert.assertEquals(1, d);
	}

	@Test
	public void testPersist() {
		Contex contex = new Contex();
		contex.setHost("legal.firefox.news.cn");
		contex.setDoctype("html");
		contex.setExpression("c:article_content");
		contex.setTemplate("legal.firefox.news.cn/[d]/[d]/[d]/[*].php");
		contex.setLastoptime(new Timestamp(System.currentTimeMillis()));
		dbServer.persist(contex);
	}
}
