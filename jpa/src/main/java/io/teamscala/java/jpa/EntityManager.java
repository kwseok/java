package io.teamscala.java.jpa;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Entity Manager Context annotation.
 *
 * @see io.teamscala.java.jpa.EntityManagerContext
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface EntityManager {

    /** @return Entity Manager Context class. */
    Class<? extends io.teamscala.java.jpa.EntityManagerContext> value();
}
