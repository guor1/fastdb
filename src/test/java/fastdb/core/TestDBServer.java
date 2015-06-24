package fastdb.core;

import java.sql.Timestamp;
import java.util.List;

import org.fastdb.DBQuery;
import org.fastdb.DBRow;
import org.fastdb.DBServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestDBServer {

	private DBServer dbServer;

	@Before
	public void init() {
		dbServer = new DBServer(H2Database.dataSource);
		String sql = "CREATE TABLE `wdyq_contex` (`id` int(10) NOT NULL AUTO_INCREMENT, `host` varchar(100) NOT NULL, `doctype` varchar(10) NULL, `template` varchar(255) NOT NULL, `expression` varchar(255) NOT NULL, `lastoptime` timestamp NULL, PRIMARY KEY (`id`), UNIQUE KEY `template_uk` (`template`) USING BTREE) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
		dbServer.createNativeQuery("DROP TABLE IF EXISTS `wdyq_contex`").executeUpdate();
		int d = dbServer.createNativeQuery(sql).executeUpdate();
		Assert.assertEquals(0, d);
	}

	@Test
	public void testDBQuery() {
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
}
