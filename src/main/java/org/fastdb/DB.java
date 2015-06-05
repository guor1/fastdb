package org.fastdb;

import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

public class DB {

	public static void persist(Object entity) {
	}

	public static <T> T merge(T entity) {
		return null;
	}

	public static void remove(Object entity) {
	}

	public static <T> T find(Class<T> entityClass, Object primaryKey) {
		return null;
	}

	public static <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return null;
	}

	public static void flush() {
	}

	public static void setFlushMode(FlushModeType flushMode) {
	}

	public static FlushModeType getFlushMode() {
		return null;
	}

	public static void lock(Object entity, LockModeType lockMode) {
	}

	public static void refresh(Object entity) {
	}

	public static void clear() {
	}

	public static boolean contains(Object entity) {
		return false;
	}

	public static Query createQuery(String qlString) {
		return null;
	}

	public static Query createNamedQuery(String name) {
		return null;
	}

	public static Query createNativeQuery(String sqlString) {
		return null;
	}

	public static <T> Query createNativeQuery(String sqlString, Class<T> resultClass) {
		return null;
	}

	public static Query createNativeQuery(String sqlString, String resultSetMapping) {
		return null;
	}

	public static void joinTransaction() {
	}

	public static Object getDelegate() {
		return null;
	}

	public static void close() {
	}

	public static boolean isOpen() {
		return false;
	}

	public static EntityTransaction getTransaction() {
		return null;
	}
}
