package io.teamscala.java.core.util;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.WeakHashMap;

import static io.teamscala.java.core.util.ClassUtils.getDefaultValue;
import static org.apache.commons.lang3.ArrayUtils.indexOf;
import static org.apache.commons.lang3.ClassUtils.isAssignable;

/**
 * 동적으로 객체의 속성, 메소드에 접근할 수 있는 접근자입니다.
 *
 * @param <T> the target type
 */
public class DynamicAccessor<T> {
    private static final Logger logger = LoggerFactory.getLogger(DynamicAccessor.class);

    /**
     * Cache for DynamicAccessor.
     */
    private static final Map<Class<?>, DynamicAccessor<?>> CACHE = new WeakHashMap<>();

    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private static final String IS_PREFIX = "is";

    @SuppressWarnings("unchecked")
    public static <T> DynamicAccessor<T> getInstance(Class<T> targetType) {
        if (CACHE.containsKey(targetType)) return (DynamicAccessor<T>) CACHE.get(targetType);
        synchronized (CACHE) {
            if (CACHE.containsKey(targetType)) return (DynamicAccessor<T>) CACHE.get(targetType);

            logger.info("First create DynamicAccessor for Class[{}]", targetType);
            DynamicAccessor<T> dynamicAccessor = new DynamicAccessor<>(targetType);
            CACHE.put(targetType, dynamicAccessor);
            return dynamicAccessor;
        }
    }

    //--

    private final ConstructorAccess<T> ctorAccessor;
    private final FieldAccess fieldAccessor;
    private final MethodAccess methodAccessor;

    public DynamicAccessor(Class<T> targetType) {
        Preconditions.checkNotNull(targetType, "'targetType' must not be null");

        this.ctorAccessor = ConstructorAccess.get(targetType);
        this.fieldAccessor = FieldAccess.get(targetType);
        this.methodAccessor = MethodAccess.get(targetType);
    }

    public T newInstance() {
        return ctorAccessor.newInstance();
    }

    public T newInstance(Object enclosingInstance) {
        return ctorAccessor.newInstance(enclosingInstance);
    }

    public String[] getFieldNames() {
        return fieldAccessor.getFieldNames();
    }

    public String[] getMethodNames() {
        return methodAccessor.getMethodNames();
    }

    public Class<?>[][] getMethodParameterTypes() {
        return methodAccessor.getParameterTypes();
    }

    public Object getField(Object instance, String fieldName) {
        return fieldAccessor.get(instance, fieldName);
    }

    public void setField(Object instance, String fieldName, Object value) {
        fieldAccessor.set(instance, fieldName, value);
    }

    public void setField(Object instance, String fieldName, boolean value) {
        fieldAccessor.setBoolean(instance, fieldAccessor.getIndex(fieldName), value);
    }

    public void setField(Object instance, String fieldName, byte value) {
        fieldAccessor.setByte(instance, fieldAccessor.getIndex(fieldName), value);
    }

    public void setField(Object instance, String fieldName, char value) {
        fieldAccessor.setChar(instance, fieldAccessor.getIndex(fieldName), value);
    }

    public void setField(Object instance, String fieldName, short value) {
        fieldAccessor.setShort(instance, fieldAccessor.getIndex(fieldName), value);
    }

    public void setField(Object instance, String fieldName, int value) {
        fieldAccessor.setInt(instance, fieldAccessor.getIndex(fieldName), value);
    }

    public void setField(Object instance, String fieldName, long value) {
        fieldAccessor.setLong(instance, fieldAccessor.getIndex(fieldName), value);
    }

    public void setField(Object instance, String fieldName, float value) {
        fieldAccessor.setFloat(instance, fieldAccessor.getIndex(fieldName), value);
    }

    public void setField(Object instance, String fieldName, double value) {
        fieldAccessor.setDouble(instance, fieldAccessor.getIndex(fieldName), value);
    }

    public Object invoke(Object instance, String methodName, Object... args) {
        return methodAccessor.invoke(instance, methodName, args);
    }

    public Object invoke(Object instance, int methodIndex, Object... args) {
        return methodAccessor.invoke(instance, methodIndex, args);
    }

    public int indexOfField(String fieldName) {
        return indexOf(getFieldNames(), fieldName);
    }

    public int indexOfMethod(String methodName) {
        return indexOf(getMethodNames(), methodName);
    }

    public int indexOfBooleanGetter(String propertyName) {
        return indexOfMethod(getBooleanGetterName(propertyName));
    }

    public int indexOfGetter(String propertyName) {
        return indexOfMethod(getGetterName(propertyName));
    }

    public int indexOfGetter(String propertyName, Class<?> returnType) {
        if (boolean.class.equals(returnType))
            return indexOfBooleanGetter(propertyName);
        else
            return indexOfGetter(propertyName);
    }

    public int indexOfSetter(String propertyName, Class<?> propertyType) {
        int index = indexOfMethod(getSetterName(propertyName));
        if (index > -1) {
            Class<?>[] parameterTypes = getMethodParameterTypes()[index];
            if (parameterTypes.length != 1) return -1;
            if (propertyType != null && !isAssignable(propertyType, parameterTypes[0])) return -1;
        }
        return index;
    }

    public int indexOfSetter(String propertyName) {
        return indexOfSetter(propertyName, null);
    }

    public boolean isReadableBooleanProperty(String propertyName) {
        return indexOfBooleanGetter(propertyName) > -1;
    }

    public boolean isReadableProperty(String propertyName) {
        return indexOfGetter(propertyName) > -1;
    }

    public boolean isReadableProperty(String propertyName, Class<?> returnType) {
        return indexOfGetter(propertyName, returnType) > -1;
    }

    public boolean isWritableProperty(String propertyName, Class<?> propertyType) {
        return indexOfSetter(propertyName, propertyType) > -1;
    }

    public boolean isWritableProperty(String propertyName) {
        return indexOfSetter(propertyName) > -1;
    }

    public Object getBooleanProperty(Object instance, String propertyName) {
        int index = indexOfBooleanGetter(propertyName);
        if (index > -1) return invoke(instance, index);
        throw new IllegalArgumentException("Unable to find public boolean getter for property: " + propertyName);
    }

    public Object getProperty(Object instance, String propertyName) {
        int index = indexOfGetter(propertyName);
        if (index > -1) return invoke(instance, index);
        throw new IllegalArgumentException("Unable to find public getter for property: " + propertyName);
    }

    public Object getProperty(Object instance, String propertyName, Class<?> returnType) {
        int index = indexOfGetter(propertyName, returnType);
        if (index > -1) return invoke(instance, index);
        throw new IllegalArgumentException("Unable to find public getter for property: " + propertyName);
    }

    public Object setProperty(Object instance, String propertyName, Object value) {
        Class<?> propertyType = value != null ? value.getClass() : null;
        int index = indexOfSetter(propertyName, propertyType);
        if (index > -1) {
            Class<?> parameterType = getMethodParameterTypes()[index][0];
            if (value == null && parameterType.isPrimitive())
                value = getDefaultValue(parameterType);

            return invoke(instance, index, value);
        }
        throw new IllegalArgumentException(
            "Unable to find public setter for property: " + propertyName +
                ", type: " + propertyType);
    }

    private static String getBooleanGetterName(String propertyName) {
        return IS_PREFIX + StringUtils.capitalize(propertyName);
    }

    private static String getGetterName(String propertyName) {
        return GET_PREFIX + StringUtils.capitalize(propertyName);
    }

    private static String getSetterName(String propertyName) {
        return SET_PREFIX + StringUtils.capitalize(propertyName);
    }
}