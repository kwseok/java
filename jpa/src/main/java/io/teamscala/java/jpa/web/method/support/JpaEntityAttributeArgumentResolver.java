package io.teamscala.java.jpa.web.method.support;

import io.teamscala.java.core.util.NumberUtils;
import io.teamscala.java.core.web.method.support.AbstractEntityAttributeArgumentResolver;
import io.teamscala.java.jpa.JpaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * JPA entity attribute argument resolver.
 *
 */
public class JpaEntityAttributeArgumentResolver extends AbstractEntityAttributeArgumentResolver {
	private static final Logger LOG = LoggerFactory.getLogger(JpaEntityAttributeArgumentResolver.class);

	public JpaEntityAttributeArgumentResolver(ConversionService conversionService) { super(conversionService); }

	@Override
	protected Object getIdentifier(MethodParameter parameter, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Class<?> entityClass = parameter.getParameterType();
		EntityManager entityManager = getEntityManager(parameter);
		EntityType<?> entityType = entityManager.getMetamodel().entity(entityClass);
		List<SingularAttribute<?, ?>> idAttributes;

		// Singular identifier.
		if (entityType.hasSingleIdAttribute()) {
			idAttributes = new ArrayList<>(1);
			idAttributes.add(entityType.getId(entityType.getIdType().getJavaType()));
		}
		// Multiple identifiers.
		else {
			Set<? extends SingularAttribute<?, ?>> idClassAttributes = entityType.getIdClassAttributes();
			idAttributes = new ArrayList<>(idClassAttributes.size());
			idAttributes.addAll(idClassAttributes);
		}

		String[] bindNestedPaths = getBindNestedPaths();
		List<String> allowedFields = new ArrayList<>(idAttributes.size() * (bindNestedPaths.length + 1));
		for (SingularAttribute<?, ?> a : idAttributes) {
			String name = a.getName();
			allowedFields.add(name);
			for (String nestedPath : bindNestedPaths) {
				allowedFields.add(name + nestedPath);
			}
		}
		LOG.debug("Allowed fields for identifier binding : " + allowedFields);

		ServletRequest servletRequest = (ServletRequest) webRequest.getNativeRequest();
		PropertyValues propertyValues = new ServletRequestParameterPropertyValues(servletRequest, getPrefixForParameter(parameter));
		Object idTarget = BeanUtils.instantiateClass(entityClass);
		WebDataBinder idBinder = binderFactory.createBinder(webRequest, idTarget, "idTarget");
		if (isDirectFieldAccess(parameter))
			idBinder.initDirectFieldAccess();
		else
			idBinder.initBeanPropertyAccess();
		idBinder.setAllowedFields(allowedFields.toArray(new String[allowedFields.size()]));
		idBinder.bind(propertyValues);

		Object id = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(idTarget);
		return NumberUtils.isZero(id) ? null : id;
	}

	@Override
	protected Object findEntity(Object identifier, MethodParameter parameter) {
		Object entity = getEntityManager(parameter).find(parameter.getParameterType(), identifier);
		return JpaHelper.getImplementation(entity);
	}

	private EntityManager getEntityManager(MethodParameter parameter) {
		return JpaHelper.getEntityManager(parameter.getParameterType());
	}
}
