package io.teamscala.java.core.util;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NotWritablePropertyException;

import java.lang.reflect.Field;

import static org.springframework.util.ReflectionUtils.*;

/**
 * Custom extension of {@link BeanWrapperImpl} that falls back to direct field access in case the object or type being
 * wrapped does not use accessor methods.
 * 
 */
public class DirectFieldAccessFallbackBeanWrapper extends BeanWrapperImpl {

    public DirectFieldAccessFallbackBeanWrapper() { super(); }
    public DirectFieldAccessFallbackBeanWrapper(boolean registerDefaultEditors) { super(registerDefaultEditors); }
    public DirectFieldAccessFallbackBeanWrapper(Class<?> clazz) { super(clazz); }
    public DirectFieldAccessFallbackBeanWrapper(Object object) { super(object); }
    public DirectFieldAccessFallbackBeanWrapper(Object object, String nestedPath, Object rootObject) { super(object, nestedPath, rootObject); }

    @Override
    public boolean isReadableProperty(String propertyName) {
        return super.isReadableProperty(propertyName) ||
                findField(getWrappedClass(), propertyName) != null;
    }

    @Override
    public boolean isWritableProperty(String propertyName) {
        return super.isWritableProperty(propertyName) ||
              findField(getWrappedClass(), propertyName) != null;
    }

    @Override
    public Object getPropertyValue(String propertyName) {
        try {
            return super.getPropertyValue(propertyName);
        } catch (NotReadablePropertyException e) {
            Field field = findField(getWrappedClass(), propertyName);
            if (field == null) throw e;
            makeAccessible(field);
            return getField(field, getWrappedInstance());
        }
    }

    @Override
    public void setPropertyValue(String propertyName, Object value) {
        try {
            super.setPropertyValue(propertyName, value);
        } catch (NotWritablePropertyException e) {
            Field field = findField(getWrappedClass(), propertyName);
            if (field == null) throw e;
            makeAccessible(field);
            setField(field, getWrappedInstance(), convertIfNecessary(value, field.getType(), field));
        }
    }
}
