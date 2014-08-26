package io.teamscala.java.core.format;

import io.teamscala.java.core.util.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * Enum converter factory.
 *
 */
public final class EnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(final Class<T> targetType) {
        return source -> {
            if (source.length() == 0) return null;

            String name = StringUtils.toCamelCase(source.trim());
            for (T e : targetType.getEnumConstants()) {
                if (StringUtils.toCamelCase(e.name()).equals(name)) return e;
            }
            throw new IllegalArgumentException("No enum constant " + targetType.getCanonicalName() + "." + name);
        };
    }
}
