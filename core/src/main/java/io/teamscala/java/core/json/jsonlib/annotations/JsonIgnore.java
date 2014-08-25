package io.teamscala.java.core.json.jsonlib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Json ignore annotation.
 *
 * @author 석기원
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface JsonIgnore {}
