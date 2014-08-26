package io.teamscala.java.core.format;

import io.teamscala.java.core.util.NumberUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import static org.springframework.beans.BeanUtils.instantiateClass;

/**
 * Abstract entity converter.
 *
 */
public abstract class AbstractEntityConverter implements GenericConverter {

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) return null;

        String string = ((String) source).trim();

        if (string.isEmpty()) return null;
        if (string.toLowerCase().equals("null")) return instantiateClass(targetType.getType());

        Object id = convertId(string, targetType);
        if (id == null || NumberUtils.isZero(id)) return null;
        else {
            Object entity = findEntity(id, targetType);
            if (entity == null)
                throw new IllegalArgumentException(String.format(
                        "No row with the given identifier exists: [%s#%s]",
                        targetType.getType().getName(), id));

            return entity;
        }
    }

    /**
     * Convert to the identifier.
     *
     * @param source the source string.
     * @param targetType the target type.
     * @return the identifier.
     */
    protected abstract Object convertId(String source, TypeDescriptor targetType);

    /**
     * Find the entity.
     *
     * @param id the identifier.
     * @param targetType the target type.
     * @return the entity.
     */
    protected abstract Object findEntity(Object id, TypeDescriptor targetType);
}
