package io.teamscala.java.jpa.json.jsonlib.filters;

import net.sf.json.util.PropertyFilter;
import org.hibernate.Hibernate;
import org.hibernate.bytecode.internal.javassist.FieldHandler;
import org.hibernate.proxy.LazyInitializer;

/**
 * Hibernate property filter.
 *
 * @author 석기원
 */
public class HibernateAwarePropertyFilter implements PropertyFilter {

	@Override public boolean apply(Object target, String name, Object value) {
        return value instanceof FieldHandler ||
                value instanceof LazyInitializer ||
                !Hibernate.isInitialized(value);
    }
}
