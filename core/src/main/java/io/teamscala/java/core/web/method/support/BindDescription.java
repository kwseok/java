package io.teamscala.java.core.web.method.support;

import io.teamscala.java.core.web.method.annotation.Bind;
import io.teamscala.java.core.web.method.annotation.BindStrategy;
import io.teamscala.java.core.web.method.annotation.BindType;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Bind Description.
 *
 * @author 석기원
 */
public class BindDescription implements Serializable {
	private static final long serialVersionUID = 8572062364525149579L;

	private final String targetName;
	private final Class<?> targetType;
	private final Bind bind;

	public BindDescription(String targetName, Class<?> targetType, Bind bind) {
		Assert.hasText(targetName, "TargetName must not be empty");
		Assert.notNull(targetType, "TargetType must not be null");
		Assert.notNull(bind, "Bind must not be null");

		this.targetName = targetName;
		this.targetType = targetType;
		this.bind = bind;
	}

	public String getTargetName() { return targetName; }
	public Class<?> getTargetType() { return targetType; }
	public BindType getType() { return bind.value(); }
	public BindStrategy getStrategy() { return bind.strategy(); }
	public String[] getNestedPaths() { return bind.nestedPaths(); }
	public boolean isPreventDefault() { return bind.preventDefault(); }
	public boolean isPreventDefaultNestedPaths() { return bind.preventDefaultNestedPaths(); }
	public Class<?>[] getGroups() { return bind.groups(); }
}
