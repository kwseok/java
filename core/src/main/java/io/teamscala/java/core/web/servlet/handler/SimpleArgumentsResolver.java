package io.teamscala.java.core.web.servlet.handler;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple arguments resolver.
 * 
 * @author 석기원
 *
 * @param <T> the target object type
 */
public class SimpleArgumentsResolver<T extends Object> implements ArgumentsResolver<T> {

	private String[] propertyNames;

	public SimpleArgumentsResolver() {}
	public SimpleArgumentsResolver(String... propertyNames) {
		this.propertyNames = propertyNames;
	}

	public String[] getPropertyNames() {
		return propertyNames;
	}

	public void setPropertyNames(String... propertyNames) {
		this.propertyNames = propertyNames;
	}

	@Override
	public Object[] resolve(T target) {
		if (target == null ||
				propertyNames == null ||
				propertyNames.length == 0) return null;

		BeanWrapper beanWrapper = new BeanWrapperImpl(target);
		List<Object> arguments = new ArrayList<>(propertyNames.length);

		for (String propertyName : propertyNames)
			if (beanWrapper.isReadableProperty(propertyName))
				arguments.add(beanWrapper.getPropertyValue(propertyName));

		return arguments.toArray();
	}
}
