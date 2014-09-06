package io.teamscala.java.core.json.jsonlib.processors;

import net.sf.json.JsonConfig;

/**
 * Json preprocessor interface.
 *
 * @param <T> the target type
 */
public interface JsonPreprocessor<T> {
    Object process(T target, JsonConfig jsonConfig);
}
