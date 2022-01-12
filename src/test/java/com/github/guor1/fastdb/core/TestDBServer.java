package com.github.guor1.fastdb.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.guor1.fastdb.DB;
import com.github.guor1.fastdb.DBQuery;
import com.github.guor1.fastdb.DBRow;
import com.github.guor1.fastdb.core.bean.Contex;
import com.github.guor1.fastdb.core.bean.User;

import java.sql.Timestamp;
import java.util.List;

public class TestDBServer {

    @Before
    public void init() {
        H2Database.initTable();
    }

    @Test
    public void testNativeQuery() {
        DBQuery dbQuery = DB.createNativeQuery("select * from wdyq_contex");
        List<DBRow> resultList = dbQuery.findList();
        Assert.assertEquals(0, resultList.size());

        dbQuery = DB
                .createNativeQuery("insert into `wdyq_contex` (`host`,`doctype`,`template`,`expression`,`user`,`lastoptime`) values (?,?,?,?,?,?)");
        int p = 1;
        dbQuery.setParameter(p++, "legal.firefox.news.cn");
        dbQuery.setParameter(p++, "html");
        dbQuery.setParameter(p++, "legal.firefox.news.cn/[d]/[d]/[d]/[*].html");
        dbQuery.setParameter(p++, "c:article_content");
        dbQuery.setParameter(p++, 1);
        dbQuery.setParameter(p++, new Timestamp(System.currentTimeMillis()));
        int d = dbQuery.executeUpdate();
        Assert.assertEquals(1, d);

        dbQuery = DB.createNativeQuery("select * from `wdyq_contex`");
        resultList = dbQuery.findList();
        Assert.assertEquals(1, resultList.size());
    }

    @Test
    public void testFind() {
        Contex contex = DB.find(Contex.class, 34);
        Assert.assertNull(contex);
    }

    @Test
    public void testFindList() {
        List<Contex> resultList = DB.findList(Contex.class);
        Assert.assertEquals(0, resultList.size());
    }

    @Test
    public void testDelete() {
        int d = DB.delete(Contex.class, 34);
        Assert.assertEquals(0, d);
    }

    @Test
    public void testPersist() {
        Contex contex = new Contex();
        contex.setHost("legal.firefox.news.cn");
        contex.setDoctype("html");
        contex.setExpression("c:article_content");
        contex.setTemplate("legal.firefox.news.cn/[d]/[d]/[d]/[*].jsp");
        contex.setLastoptime(new Timestamp(System.currentTimeMillis()));
        contex.setUser(new User(1));
        DB.persist(contex);
        Assert.assertEquals(0, contex.getId());
    }
}
