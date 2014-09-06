package io.teamscala.java.jpa;

import javax.persistence.EntityManager;

/**
 * Entity Manager Context interface.
 */
public interface EntityManagerContext {
    /**
     * @return the entity manager.
     */
    EntityManager getEntityManager();
}
