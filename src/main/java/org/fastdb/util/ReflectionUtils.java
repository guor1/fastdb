package org.fastdb.util;

import static java.util.Locale.ENGLISH;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

	public static <K, T extends Annotation> boolean annotationed(Class<K> klass, Class<T> annotation) {
		return getAnnotation(klass, annotation) != null;
	}

	public static <K, T extends Annotation> T getAnnotation(Class<K> klass, Class<T> annotation) {
		return klass.getAnnotation(annotation);
	}

	public static <T extends Annotation> boolean annotationed(Field field, Class<T> annotation) {
		return getAnnotation(field, annotation) != null;
	}

	public static <T extends Annotation> T getAnnotation(Field field, Class<T> annotation) {
		return field.getAnnotation(annotation);
	}

	/**
	 * getter/setter三要素：返回类型一致、参数个数一致、方法名一致
	 */
	public static Method findGetter(Field field, Method[] declaredMethods) {
		Class<?> fieldType = field.getType();
		String fieldName = field.getName();
		String readMethodName = fieldType.equals(boolean.class) ? "is" + capitalize(fieldName) : "get" + capitalize(fieldName);

		for (int i = 0; i < declaredMethods.length; i++) {
			Method m = declaredMethods[i];
			// 方法名一致
			if (!m.getName().equals(readMethodName)) {
				continue;
			}
			Class<?>[] params = m.getParameterTypes();
			// getter方法无参数，而且返回值一致
			if (params.length == 0 && field.getType().equals(m.getReturnType())) {
				int modifiers = m.getModifiers();
				if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
					return m;
				}
			}
		}
		return null;
	}

	public static Method findSetter(Field field, Method[] declaredMethods) {
		String fieldName = field.getName();
		String writeMethodName = "set" + capitalize(fieldName);
		for (int i = 0; i < declaredMethods.length; i++) {
			Method m = declaredMethods[i];
			// 方法名一致
			if (!m.getName().equals(writeMethodName)) {
				continue;
			}
			Class<?>[] params = m.getParameterTypes();
			// setter方法有一个参数，无返回值
			if (params.length == 1 && field.getType().equals(params[0]) && void.class.equals(m.getReturnType())) {
				int modifiers = m.getModifiers();
				if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
					return m;
				}
			}
		}
		return null;
	}

	public static String capitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
	}
}
