#Fastdb：极速DB


##特性：


1. 运行高效，配置简单，这是所有API设计的最基本原则
2. 支持多数据源，且配置简单，基本上就只有c3p0数据库连接池的配置
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
