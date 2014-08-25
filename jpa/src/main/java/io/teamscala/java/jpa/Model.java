package io.teamscala.java.jpa;

import com.google.common.base.Objects;
import com.mysema.query.annotations.QueryExclude;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * Base-class for JPA-mapped models that provides convenience methods.
 *
 * @param <ID> the identifier type.
 */
@QueryExclude
public abstract class Model<ID> {

	/**
	 * @return the identifier.
	 */
	public abstract ID getId();

	/**
	 * @return the identifier for reference after checked.
	 */
	@SuppressWarnings("unchecked")
	public final ID refId() {
		ID id = (this instanceof HibernateProxy)
                ? (ID) ((HibernateProxy) this).getHibernateLazyInitializer().getIdentifier()
                : getId();

		if (id instanceof Model)
			throw new IllegalStateException("Invalid identifier class, Must not be used Model.");

		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (this == obj) return true;
		if (!(obj instanceof Model)) return false;

		Model<?> that = (Model<?>) obj;
		if (!JpaHelper.getClass(this).isAssignableFrom(JpaHelper.getClass(that))) return false;

		ID id = refId();
		return id != null && id.equals(that.refId());
	}

	@Override public int hashCode() { return Objects.hashCode(refId()); }

	@Override
	public String toString() {
		return String.format("Entity of type %s with identifier: %s",
                JpaHelper.getClass(this).getName(), refId());
	}

	//--

	/**
     * Persists (inserts) this entity.
     */
    public void persist() { persist(entityManager()); }

    /**
     * Persists (inserts) this entity.
     *
     * @param em the entity manager to use
     */
    @Transactional
    public void persist(EntityManager em) { em.persist(this); }

    /**
     * Merges this entity.
     */
    public void merge() { merge(entityManager()); }

    /**
     * Merges this entity.
     * 
     * @param em the entity manager to use
     */
    @Transactional
    public void merge(EntityManager em) { em.merge(this); }

    /**
     * Saves this entity.
     */
    public void save() { save(entityManager()); }

    /**
     * Saves this entity.
     * 
     * @param em the entity manager to use
     */
    @Transactional
    public void save(EntityManager em) {
    	if (refId() == null)
    		em.persist(this);
    	else
    		em.merge(this);
    }

    /**
     * Removes this entity.
     */
    public void remove() { remove(entityManager()); }

    /**
     * Removes this entity.
     * 
     * @param em the entity manager to use
     */
    @Transactional
    public void remove(EntityManager em) { em.remove(this); }

    /**
     * Refreshes this entity from the database.
     */
    public void refresh() { refresh(entityManager()); }

    /**
     * Refreshes this entity from the database.
     * 
     * @param em the entity manager to use
     */
    @Transactional
    public void refresh(EntityManager em) { em.refresh(this); }

	// Entity Manager

    /**
     * @return Get the entity manager.
     */
    protected EntityManager entityManager() {
    	if (entityManager == null) {
    		entityManager = JpaHelper.getEntityManager(this);
        }
    	return entityManager;
    }
	transient EntityManager entityManager;
}
