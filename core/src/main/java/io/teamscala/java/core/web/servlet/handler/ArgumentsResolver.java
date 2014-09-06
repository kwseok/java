package io.teamscala.java.core.web.servlet.handler;

/**
 * Arguments resolver interface.
 *
 * @param <T> the target object type
 */
public interface ArgumentsResolver<T extends Object> {
    Object[] resolve(T target);
}
