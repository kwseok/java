package io.teamscala.java.core.web.method.annotation;

import io.teamscala.java.core.data.Pageable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to set defaults when injecting a {@link Pageable} into a
 * controller method.
 *
 * @author Oliver Gierke
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PageableDefaults {

    /**
     * The default-size the injected
     * {@link Pageable} should get if no
     * corresponding parameter defined in request (default is {@link Pageable#DEFAULT_SIZE}).
     *
     * @return default-size
     */
    int value() default Pageable.DEFAULT_SIZE;

    /**
     * The default-index the injected
     * {@link Pageable} should get if no corresponding
     * parameter defined in request (default is 0).
     *
     * @return default-index
     */
    int index() default 0;

    /**
     * The default-boundSize the injected
     * {@link Pageable} should get if no
     * corresponding parameter defined in request (default is {@link Pageable#DEFAULT_BOUND_SIZE}).
     *
     * @return default-boundSize
     */
    int boundSize() default Pageable.DEFAULT_BOUND_SIZE;

    /**
     * The default-sort the injected
     * {@link Pageable} should get if no corresponding
     * parameter defined in request
     *
     * @return default-sort
     */
    String sort() default "";
}
