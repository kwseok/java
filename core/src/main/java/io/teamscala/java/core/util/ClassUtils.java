package io.teamscala.java.core.util;

import org.springframework.util.Assert;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

/**
 * Extends ClassUtils.
 *
 * @author 석기원
 */
public abstract class ClassUtils extends org.apache.commons.lang3.ClassUtils {

    public static boolean isSimpleType(Class<?> type) {
        Assert.notNull(type, "Type must not be null");
        return isPrimitiveOrWrapper(type)
                || type.isEnum()
                || CharSequence.class.isAssignableFrom(type)
                || Number.class.isAssignableFrom(type)
                || Date.class.isAssignableFrom(type)
                || type.equals(URI.class)
                || type.equals(URL.class)
                || type.equals(Locale.class)
                || type.equals(Class.class);
    }

    public static Object getDefaultValue(Class<?> type) {
        if (boolean.class.equals(type)) return false;
        if (byte.class.equals(type)) return (byte) 0;
        if (char.class.equals(type)) return (char) 0;
        if (short.class.equals(type)) return (short) 0;
        if (int.class.equals(type)) return 0;
        if (long.class.equals(type)) return 0l;
        if (float.class.equals(type)) return 0.0f;
        if (double.class.equals(type)) return 0.0d;
        return null;
    }
}
