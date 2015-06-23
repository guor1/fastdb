package fastdb.core;

import org.fastdb.internal.BeanDescriptor;
import org.junit.Test;

public class TestBeanDescriptor {
	@Test
	public void test() {
		BeanDescriptor<User> beanDescriptor = new BeanDescriptor<User>(User.class);
		System.out.println(beanDescriptor.getFindByIdSql());
		System.out.println(beanDescriptor.getDeleteByIdSql());
		System.out.println(beanDescriptor.getInsertSql());
	}
}
