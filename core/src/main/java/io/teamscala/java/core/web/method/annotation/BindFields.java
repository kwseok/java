package io.teamscala.java.core.web.method.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bind fields annotation.
 *
 * @author 석기원
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface BindFields {

    /**
     * Supports "xxx*", "*xxx" and "*xxx*" patterns.
     *
     * @return field names.
     */
    String[] value() default {};

    /**
     * @return bind strategy.
     */
    BindStrategy strategy() default BindStrategy.ALWAYS;
}
