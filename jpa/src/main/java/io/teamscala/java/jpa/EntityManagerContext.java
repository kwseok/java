package io.teamscala.java.jpa;

import javax.persistence.EntityManager;

/**
 * Entity Manager Context interface.
 *
 * @author 석기원
 */
public interface EntityManagerContext {
    /** @return the entity manager. */
    EntityManager getEntityManager();
}
