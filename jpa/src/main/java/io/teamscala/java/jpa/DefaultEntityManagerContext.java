package io.teamscala.java.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Default Entity Manager Context.
 */
public class DefaultEntityManagerContext implements EntityManagerContext {

    @PersistenceContext
    transient EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Configurable Default Entity Manager Context.
     *
     * @see org.springframework.beans.factory.annotation.Configurable
     */
    @org.springframework.beans.factory.annotation.Configurable
    public static class Configurable extends DefaultEntityManagerContext {
    }
}
