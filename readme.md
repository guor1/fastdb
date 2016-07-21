#fastdb

##特性：

1. 运行高效，配置简单，这是所有API设计的最基本原则
2. 支持多数据源，且配置简单，基本上就只有数据库连接池的配置
3. 支持手写sql
4. 不完全面向对象
5. 对象映射支持JPA规范
6. 方便与spring无缝集成
7. 功能单一，只处理数据库操作
8. 支持懒加载
9. 体积小，当前版本只有38K

##现有ORM分析（mybatis、hibernate）：

1. 配置复杂
2. 不易支持多数据源
3. 要学太多语法
4. 封装过度

##主张：

1. 不管怎么架构，sql一定要掌握在我们自己手中，为后续可能存在的优化留有余地
2. 学习一个全新的框架，学习成本越低越好
3. 不做过度的封装，如今的框架随着版本的升级，普遍存在越来越臃肿的问题，因为兼容太多情形而导致效率低

##示例：

配置文件
```
fastdb.debug.sql=true
fastdb.default=h2db

#fastdb.h2db.acquireIncrement=1
#fastdb.h2db.checkoutTimeout=5000
#fastdb.h2db.initialPoolSize=3
#fastdb.h2db.maxIdleTime=60
#fastdb.h2db.maxPoolSize=15
#fastdb.h2db.minPoolSize=3
fastdb.h2db.driverClass=org.h2.Driver
fastdb.h2db.jdbcUrl=jdbc:h2:~/test
fastdb.h2db.user=root
fastdb.h2db.password=root
```

实体类
```
import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * wdyq_contex
 * 
 * @author guor
 * 
 */
@Entity
@Table(name = "wdyq_contex")
public class Contex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7980715878136339361L;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "host", nullable = false, length = 100)
	private String host;

	@Column(name = "doctype", nullable = true, length = 10)
	private String doctype;

	@Column(name = "template", nullable = false, unique = true, length = 255)
	private String template;

	/**
	 * i:content c:page content t:article r:<div>(.*)</div>
	 */
	@Column(name = "expression", nullable = false, length = 255)
	private String expression;

	@ManyToOne
	@JoinColumn(name = "user")
	private User user;

	@Version
	private Timestamp lastoptime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getLastoptime() {
		return lastoptime;
	}

	public void setLastoptime(Timestamp lastoptime) {
		this.lastoptime = lastoptime;
	}

	@Override
	public String toString() {
		return "Contex [id=" + id + ", host=" + host + ", doctype=" + doctype + ", template=" + template + ", expression=" + expression + "]";
	}
}
```

测试用例
```
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

		DBRow dbRow = DB.createNativeQuery("SELECT * FROM `wdyq_user` WHERE `username`=?").setParameter(1, "guor").findUnique();
		Long id = Long.parseLong(dbRow.get("ID").toString());

		Contex contex = new Contex();
		contex.setHost("legal.firefox.news.cn");
		contex.setDoctype("html");
		contex.setExpression("c:article_content");
		contex.setTemplate("legal.firefox.news.cn/[d]/[d]/[d]/[*].jsp");
		contex.setUser(new User(id));
		DB.persist(contex);
	}

	@Test
	public void testFindList() {
		List<Contex> list = DB.findList(Contex.class);
		Assert.assertEquals(0, list.size());
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
```
