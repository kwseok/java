package io.teamscala.java.core.util;

public abstract class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

    public static boolean isZero(Object input) {
        return (input instanceof Number) && ((Number) input).doubleValue() == 0.0d;
    }
}
