package io.teamscala.java.jpa.format;

import io.teamscala.java.core.format.AbstractEntityConverter;
import io.teamscala.java.core.util.DirectFieldAccessFallbackBeanWrapper;
import io.teamscala.java.core.util.StringUtils;
import io.teamscala.java.jpa.JpaHelper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * JPA entity converter.
 *
 * @author 석기원
 */
public class JpaEntityConverter extends AbstractEntityConverter implements InitializingBean {

    private final ConversionService conversionService;

    public JpaEntityConverter(ConversionService conversionService) {
        Assert.notNull(conversionService, "'conversionService' must not be null");
        this.conversionService = conversionService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (conversionService == null) throw new IllegalArgumentException("'conversionService' is required");
    }

    public ConversionService getConversionService() { return conversionService; }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, io.teamscala.java.jpa.Model.class));
    }

    @Override
    protected Object convertId(String source, TypeDescriptor targetType) {
        Class<?> entityClass = targetType.getType();
        EntityManager entityManager = getEntityManager(targetType);
        EntityType<?> entityType = entityManager.getMetamodel().entity(entityClass);

        // Singular identifier.
        if (entityType.hasSingleIdAttribute()) {
            Class<?> idType = entityType.getIdType().getJavaType();

            if (conversionService.canConvert(source.getClass(), idType))
                return conversionService.convert(source, idType);

            // Maybe that embedded identifier.

            // Source convert to Map
            Map<String, String> rawIdMap = StringUtils.toMap(source);
            // Bean wrapper for identifier.
            BeanWrapper idWrapper = new DirectFieldAccessFallbackBeanWrapper(idType);
            // Set the conversion service.
            idWrapper.setConversionService(conversionService);
            // Raw map convert to identifier.
            for (PropertyDescriptor p : idWrapper.getPropertyDescriptors()) {
                String propertyName = p.getName();
                if (rawIdMap.containsKey(propertyName))
                    idWrapper.setPropertyValue(propertyName, rawIdMap.get(propertyName));
            }
            return idWrapper.getWrappedInstance();
        }

        // Multiple identifiers.

        // Source convert to Map
        Map<String, String> rawIdMap = StringUtils.toMap(source);
        // Bean wrapper for entity.
        BeanWrapper entityWrapper = new DirectFieldAccessFallbackBeanWrapper(entityClass);
        // Set the conversion service.
        entityWrapper.setConversionService(conversionService);
        // Raw map convert to identifier.
        for (SingularAttribute<?, ?> a : entityType.getIdClassAttributes()) {
            String propertyName = a.getName();
            entityWrapper.setPropertyValue(propertyName, rawIdMap.get(propertyName));
        }

        return entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entityWrapper.getWrappedInstance());
    }

    @Override
    protected Object findEntity(Object id, TypeDescriptor targetType) {
        return getEntityManager(targetType).find(targetType.getType(), id);
    }

    private EntityManager getEntityManager(TypeDescriptor targetType) {
        return JpaHelper.getEntityManager(targetType.getType());
    }
}
