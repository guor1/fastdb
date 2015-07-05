package org.fastdb.core.impl;

import java.lang.reflect.Field;

import org.fastdb.bean.BeanDescriptor;
import org.fastdb.bean.property.DefaultBeanProperty;

/**
 * There are two ways to define the id property.
 * <p>
 * Example 1: use default colum name `id`
 * <p>
 * &#064; Id <br>
 * private long id;
 * <p>
 * Example 2: custom colum name
 * <p>
 * &#064; Id <br>
 * &#064; Column(name="tid", columnDefinition="CLOB NOT NULL") <br>
 * private long id;
 * 
 * @author guor
 *
 */
public class BeanPropertyId extends DefaultBeanProperty {

	public BeanPropertyId(BeanDescriptor<?> beanDescriptor, Field field) {
		super(beanDescriptor, field);
	}
}
