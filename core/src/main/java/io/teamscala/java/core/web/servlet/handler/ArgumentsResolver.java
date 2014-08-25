package io.teamscala.java.core.web.servlet.handler;

/**
 * Arguments resolver interface.
 * 
 * @author 석기원
 *
 * @param <T> the target object type
 */
public interface ArgumentsResolver<T extends Object> {
	Object[] resolve(T target);
}
