package io.teamscala.java.core.format;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.Assert;

import java.text.NumberFormat;
import java.text.ParseException;

import static org.springframework.util.NumberUtils.convertNumberToTargetClass;

/**
 * Number converter factory.
 */
public final class NumberConverterFactory implements ConverterFactory<String, Number> {
    private final NumberFormat numberFormat;

    public NumberConverterFactory() {
        this(NumberFormat.getInstance());
    }

    public NumberConverterFactory(NumberFormat numberFormat) {
        Assert.notNull(numberFormat, "NumberFormat must not be null");
        this.numberFormat = numberFormat;
    }

    @Override
    public <T extends Number> Converter<String, T> getConverter(final Class<T> targetType) {
        return source -> {
            if (source.length() == 0) return null;
            try {
                return convertNumberToTargetClass(numberFormat.parse(source), targetType);
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        };
    }
}
