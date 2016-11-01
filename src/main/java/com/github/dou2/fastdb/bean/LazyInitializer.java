package com.github.dou2.fastdb.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import com.github.dou2.fastdb.DB;
import com.github.dou2.fastdb.DBServer;
import com.github.dou2.fastdb.FastdbException;

/**
 * To lazy load an object by ID property. Here is a passage about lazy load.
 * 
 * http://blog.csdn.net/abguorui0928/article/details/34558897
 * 
 * @author guor
 *
 */
public class LazyInitializer implements MethodHandler {

	private final DBServer dbServer;
	private final Class<?> klass;
	private final Object primaryKey;

	private LazyInitializer(final DBServer dbServer, final Class<?> klass, Object primaryKey) {
		this.dbServer = dbServer;
		this.klass = klass;
		this.primaryKey = primaryKey;
	}

	/**
	 * 获取一个动态代理对象
	 * 
	 * @param klass
	 *            被代理的类
	 * @param primaryKey
	 *            数据的唯一标识
	 * @return 返回代理的对象
	 */
	public static ProxyObject load(final DBServer dbServer, final Class<?> klass, Object primaryKey) {
		final LazyInitializer instance = new LazyInitializer(dbServer == null ? DB.usePrimaryDBServer() : dbServer, klass, primaryKey);
		ProxyFactory factory = new ProxyFactory();
		factory.setSuperclass(klass);
		/**
		 * 跳过finalize方法的代理
		 */
		factory.setFilter(FINALIZE_FILTER);

		Class<?> cl = factory.createClass();
		try {
			ProxyObject obj = (ProxyObject) cl.newInstance();
			obj.setHandler(instance);
			return obj;
		} catch (Exception e) {
			throw new FastdbException(e);
		}
	}

	/**
	 * 方法过滤器，动态创建的代理类，不代理finalize方法
	 */
	private static final MethodFilter FINALIZE_FILTER = new MethodFilter() {
		public boolean isHandled(Method m) {
			return !(m.getParameterTypes().length == 0 && m.getName().equals("finalize"));
		}
	};

	@Override
	public Object invoke(final Object proxy, final Method thisMethod, final Method proceed, final Object[] args) throws Throwable {
		/**
		 * 从数据库加载数据
		 */
		Object target = this.dbServer.find(klass, primaryKey);
		if (target == null) {
			return null;
		}
		try {
			final Object returnValue;
			if (Modifier.isPublic(thisMethod.getModifiers())) {
				if (!thisMethod.getDeclaringClass().isInstance(target)) {
					throw new ClassCastException(target.getClass().getName());
				}
				returnValue = thisMethod.invoke(target, args);
			} else {
				if (!thisMethod.isAccessible()) {
					thisMethod.setAccessible(true);
				}
				returnValue = thisMethod.invoke(target, args);
			}
			return returnValue == target ? proxy : returnValue;
		} catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}
}
