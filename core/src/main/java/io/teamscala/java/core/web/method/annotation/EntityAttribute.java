package io.teamscala.java.core.web.method.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Entity attribute annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface EntityAttribute {

    /**
     * @return The name of the model attribute to bind to.
     */
    String value() default "";

    /**
     * @return The prefix for parameters (the full prefix will consist of this plus the separator)
     */
    String prefix() default "";

    /**
     * @return Direct field access flag. (Default is false)
     */
    boolean directFieldAccess() default false;

    /**
     * @return Reject if entity not found. (Default is true)
     */
    boolean rejectIfNotFound() default true;

    /**
     * @return Entity not found message code.
     */
    String notFoundMessage() default "errors.entityNotFound";

    /**
     * @return Include bind fields.
     */
    BindFields[] includes() default {};

    /**
     * @return Exclude bind fields.
     */
    BindFields[] excludes() default {};

    /**
     * @return Annotation driven flag. (Default is false)
     * @see Bind
     */
    boolean annotationDriven() default true;

    /**
     * @return Bind groups.
     */
    Class<?>[] groups() default {};
}
