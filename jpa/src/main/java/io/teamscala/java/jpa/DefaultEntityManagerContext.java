package io.teamscala.java.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Default Entity Manager Context.
 *
 * @author 석기원
 */
public class DefaultEntityManagerContext implements EntityManagerContext {

	@PersistenceContext transient EntityManager entityManager;

	@Override public EntityManager getEntityManager() { return entityManager; }

	/**
	 * Configurable Default Entity Manager Context.
	 *
	 * @author 석기원
	 * 
	 * @see org.springframework.beans.factory.annotation.Configurable
	 */
	@org.springframework.beans.factory.annotation.Configurable
	public static class Configurable extends DefaultEntityManagerContext {}
}
