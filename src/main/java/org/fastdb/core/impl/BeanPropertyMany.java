package org.fastdb.core.impl;

import java.lang.reflect.Field;

import org.fastdb.bean.BeanDescriptor;
import org.fastdb.bean.property.DefaultBeanProperty;

public class BeanPropertyMany extends DefaultBeanProperty {

	public BeanPropertyMany(BeanDescriptor<?> beanDescriptor, Field field) {
		super(beanDescriptor, field);
	}
}
