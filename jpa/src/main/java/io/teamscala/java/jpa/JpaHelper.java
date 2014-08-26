package io.teamscala.java.jpa;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.HibernateProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * JPA helper.
 *
 */
public class JpaHelper implements ApplicationContextAware {
	private static final Logger LOG = LoggerFactory.getLogger(JpaHelper.class);
	
	/**
	 * The cache for Entity Manager Contexts.
	 */
	private static final Map<Class<?>, Class<? extends EntityManagerContext>> EMC_CACHE = new WeakHashMap<>();

	/**
	 * Get the {@link EntityManagerContext}.
	 * 
	 * @param entityClass the entity class.
	 * @return {@link EntityManagerContext}
	 *
     * @see io.teamscala.java.jpa.EntityManager
	 * @see io.teamscala.java.jpa.EntityManagerContext
	 */
	public static Class<? extends EntityManagerContext> getEntityManagerContext(Class<?> entityClass) {
		if (EMC_CACHE.containsKey(entityClass)) return EMC_CACHE.get(entityClass);
		synchronized (EMC_CACHE) {
			if (EMC_CACHE.containsKey(entityClass)) return EMC_CACHE.get(entityClass);

			LOG.info("First Analyzing EntityManagerContext for Class[{}]", entityClass);
			io.teamscala.java.jpa.EntityManager annot =
                    AnnotationUtils.findAnnotation(entityClass, io.teamscala.java.jpa.EntityManager.class);
			Class<? extends EntityManagerContext> emc =
                    annot != null ? annot.value() : DefaultEntityManagerContext.class;
			EMC_CACHE.put(entityClass, emc);
			return emc;
		}
	}

	/** Application context. */
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		JpaHelper.applicationContext = applicationContext;
	}

	/**
	 * Get the {@link EntityManager}.
	 * 
	 * @param entityClass the entity class.
	 * @return {@link EntityManager}
	 * 
	 * @see io.teamscala.java.jpa.EntityManager
	 * @see io.teamscala.java.jpa.EntityManagerContext
	 */
	public static EntityManager getEntityManager(Class<?> entityClass) {
		Class<? extends EntityManagerContext> emc = getEntityManagerContext(entityClass);

		if (applicationContext != null) {
			EntityManager em = applicationContext.getBean(emc).getEntityManager();
			if (em == null)
				throw new IllegalStateException("Entity manager has not been injected from [" + emc + "]");

			return em;
		}

		if (emc == DefaultEntityManagerContext.class)
			emc = DefaultEntityManagerContext.Configurable.class;

		EntityManager em = BeanUtils.instantiateClass(emc).getEntityManager();
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected from [" + emc.getName() + "]" +
					" (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");

		return em;
	}

	/**
	 * Get the {@link EntityManager}.
	 * 
	 * @param entity the entity object.
	 * @return {@link EntityManager}
	 * 
	 * @see io.teamscala.java.jpa.EntityManager
	 * @see io.teamscala.java.jpa.EntityManagerContext
	 */
	public static EntityManager getEntityManager(Object entity) { return getEntityManager(getClass(entity)); }

	/**
	 * Check if the proxy.
     *
     * @param proxy the proxy of entity.
     * @return true or false
     */
	public static boolean isProxy(Object proxy) { return (proxy instanceof HibernateProxy); }

    /**
	 * Get the true, underlying class of a proxied persistent class.
     *
     * @param proxy the proxy of entity.
     * @return the entity class.
     */
	public static Class<?> getClass(Object proxy) { return HibernateProxyHelper.getClassWithoutInitializingProxy(proxy); }

	/**
	 * Check if the proxy is initialized.
     *
     * @param proxy the proxy of entity.
     * @return true or false
     */
	public static boolean isInitialized(Object proxy) { return Hibernate.isInitialized(proxy); }

	/**
	 * Force initialization of a proxy.
     *
     * @param proxy the proxy of entity.
     */
	public static void initialize(Object proxy) { Hibernate.initialize(proxy); }

	/**
	 * Return the underlying persistent object, initializing if necessary
     *
     * @param proxy the proxy of entity.
     * @return the entity object.
     */
	public static Object getImplementation(Object proxy) {
		if (proxy instanceof HibernateProxy) {
            return ((HibernateProxy) proxy).getHibernateLazyInitializer().getImplementation();
        }
		return proxy;
	}
}
