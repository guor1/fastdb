package org.fastdb.core.impl;

import java.lang.reflect.Field;

import org.fastdb.bean.BeanDescriptor;
import org.fastdb.bean.property.DefaultBeanProperty;

public class BeanPropertyOne extends DefaultBeanProperty {

	public BeanPropertyOne(BeanDescriptor<?> beanDescriptor, Field field) {
		super(beanDescriptor, field);
	}
}
