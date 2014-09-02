package io.teamscala.java.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Identifier accessor for JPA-mapped models.
 *
 * @param <ID> the identifier type.
 */
public class ModelIdAccessor<ID> {
    private static final Logger LOG = LoggerFactory.getLogger(ModelIdAccessor.class);

    /**
     * The cache for Model identifier accessors.
     */
    private static final Map<Class<?>, ModelIdAccessor<?>> ID_ACCESSOR_CACHE = new WeakHashMap<>();

    /**
     * Get the identifier accessor for Model.
     *
     * @param modelClass the model class.
     * @return {@link io.teamscala.java.jpa.ModelIdAccessor}
     */
    @SuppressWarnings("unchecked")
    public static <ID> ModelIdAccessor<ID> get(Class<? extends Model<ID>> modelClass) {
        if (ID_ACCESSOR_CACHE.containsKey(modelClass)) {
            return (ModelIdAccessor<ID>) ID_ACCESSOR_CACHE.get(modelClass);
        }
        synchronized (ID_ACCESSOR_CACHE) {
            if (ID_ACCESSOR_CACHE.containsKey(modelClass)) {
                return (ModelIdAccessor<ID>) ID_ACCESSOR_CACHE.get(modelClass);
            }
            LOG.info("Create identifier accessor for Model[{}]", modelClass);
            Class<ID> idClass = (Class<ID>) GenericTypeResolver.resolveTypeArguments(modelClass, Model.class)[0];
            ModelIdAccessor<ID> idAccessor = new ModelIdAccessor<>(idClass, modelClass);
            ID_ACCESSOR_CACHE.put(modelClass, idAccessor);
            return idAccessor;
        }
    }

    @SuppressWarnings("unchecked")
    public static <ID, M extends Model<ID>> ModelIdAccessor<ID> get(M model) {
        return get((Class<? extends Model<ID>>) JpaHelper.getClass(model));
    }

    // Instance area

    private final Class<ID> idClass;
    private final List<PropertyDescriptor> idProperties;

    @SuppressWarnings("unchecked")
    private ModelIdAccessor(Class<ID> idClass, Class<? extends Model<ID>> modelClass) {
        List<PropertyDescriptor> idProperties = new ArrayList<>();
        ReflectionUtils.doWithFields(modelClass, field -> {
            if (isIdAnnotationPresent(field)) {
                idProperties.add(getProperty(modelClass, field.getName()));
            }
        }, field -> !field.getDeclaringClass().equals(Model.class));
        if (idProperties.isEmpty()) {
            for (PropertyDescriptor property : BeanUtils.getPropertyDescriptors(modelClass)) {
                Method readMethod = property.getReadMethod();
                if (readMethod != null && isIdAnnotationPresent(readMethod)) {
                    idProperties.add(checkProperty(property, modelClass));
                }
            }
        }
        if (idProperties.isEmpty()) {
            throw new IllegalArgumentException("No @javax.persistence.Id property found in class [" + modelClass + "]");
        }
        this.idClass = idClass;
        this.idProperties = Collections.unmodifiableList(idProperties);
    }

    public <M extends Model<ID>> ID getId(M model) {
        try {
            if (idProperties.size() > 1) {
                BeanWrapper idWrapper = new BeanWrapperImpl(idClass);
                for (PropertyDescriptor idProperty : idProperties) {
                    idWrapper.setPropertyValue(idProperty.getName(), idProperty.getReadMethod().invoke(model));
                }
                return idClass.cast(idWrapper.getWrappedInstance());
            } else {
                return idClass.cast(idProperties.get(0).getReadMethod().invoke(model));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public <M extends Model<ID>> void setId(M model, ID id) {
        if (idProperties.size() > 1) {
            BeanWrapper modelWrapper = new BeanWrapperImpl(model);
            BeanWrapper idWrapper = new BeanWrapperImpl(id);
            for (PropertyDescriptor idProperty : idProperties) {
                modelWrapper.setPropertyValue(idProperty.getName(), idWrapper.getPropertyValue(idProperty.getName()));
            }
        } else {
            try {
                idProperties.get(0).getWriteMethod().invoke(model, id);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isIdAnnotationPresent(AccessibleObject o) {
        return o.isAnnotationPresent(Id.class) || o.isAnnotationPresent(EmbeddedId.class);
    }

    private PropertyDescriptor getProperty(Class<? extends Model<ID>> modelClass, String propertyName) {
        PropertyDescriptor property = BeanUtils.getPropertyDescriptor(modelClass, propertyName);
        if (property == null) {
            throw new InvalidPropertyException(modelClass, propertyName, "No property '" + propertyName + "' found");
        }
        return checkProperty(property, modelClass);
    }

    private PropertyDescriptor checkProperty(PropertyDescriptor property, Class<? extends Model<ID>> modelClass) {
        if (property.getReadMethod() == null) throw new NotReadablePropertyException(modelClass, property.getName());
        if (property.getWriteMethod() == null) throw new NotWritablePropertyException(modelClass, property.getName());
        return property;
    }
}
