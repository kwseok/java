package io.teamscala.java.core.web.method.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Bind annotation.
 *
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Bind {

    /**
     * @return bind type.
     */
    BindType value();

    /**
     * @return bind strategy.
     */
    BindStrategy strategy() default BindStrategy.ALWAYS;

    /**
     * Supports "xxx*", "*xxx" and "*xxx*" patterns.
     *
     * @return Nested paths.
     */
    String[] nestedPaths() default {};

    /**
     * @return prevent default. (Default is false)
     */
    boolean preventDefault() default false;

    /**
     * Supports ".*" and "[*]*" patterns.
     *
     * @return prevent default nested paths. (Default is false)
     */
    boolean preventDefaultNestedPaths() default false;

    /**
     * @return bind groups.
     */
    Class<?>[] groups() default {};

    /**
     * Defines several <code>@Bind</code> annotations on the same element
     * @see Bind
     *
     */
    @Target({FIELD, METHOD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Bind[] value();
    }
}
