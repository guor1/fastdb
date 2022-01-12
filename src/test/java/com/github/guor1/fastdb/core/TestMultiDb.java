package com.github.guor1.fastdb.core;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.guor1.fastdb.DB;
import com.github.guor1.fastdb.core.bean.Contex;

public class TestMultiDb {

	@BeforeClass
	public static void init() {
		DB.use("source").createNativeQuery("DROP TABLE IF EXISTS `wdyq_contex`").executeUpdate();
		DB.use("source")
				.createNativeQuery(
						"CREATE TABLE `wdyq_contex` (`id` int(10) NOT NULL AUTO_INCREMENT, `host` varchar(100) NOT NULL, `doctype` varchar(10) NULL, `template` varchar(255) NOT NULL, `expression` varchar(255) NOT NULL,`user` int(10) NOT NULL, `lastoptime` timestamp NULL, PRIMARY KEY (`id`), UNIQUE KEY `template_uk` (`template`) USING BTREE) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8")
				.executeUpdate();
	}

	@Test
	public void testMain() {
		List<Contex> list = DB.use("source").findList(Contex.class);
		Assert.assertEquals(0, list.size());
	}
}
