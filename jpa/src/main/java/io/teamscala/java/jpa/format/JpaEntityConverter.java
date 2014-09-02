package io.teamscala.java.jpa.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.teamscala.java.core.format.AbstractEntityConverter;
import io.teamscala.java.jpa.JpaHelper;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * JPA entity converter.
 */
public class JpaEntityConverter extends AbstractEntityConverter {

    private final ConversionService conversionService;
    private final ObjectMapper objectMapper;

    public JpaEntityConverter(ConversionService conversionService, ObjectMapper objectMapper) {
        Assert.notNull(conversionService, "'conversionService' must not be null");
        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.conversionService = conversionService;
        this.objectMapper = objectMapper;
    }

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

            // Simple type identifier.
            if (conversionService.canConvert(source.getClass(), idType)) {
                return conversionService.convert(source, idType);
            }

            // Maybe that embedded identifier.
            try {
                return objectMapper.readValue(source, idType);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            // Multiple identifiers.
            try {
                return entityManager
                    .getEntityManagerFactory()
                    .getPersistenceUnitUtil()
                    .getIdentifier(objectMapper.readValue(source, entityClass));
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    protected Object findEntity(Object id, TypeDescriptor targetType) {
        return getEntityManager(targetType).find(targetType.getType(), id);
    }

    private EntityManager getEntityManager(TypeDescriptor targetType) {
        return JpaHelper.getEntityManager(targetType.getType());
    }
}
