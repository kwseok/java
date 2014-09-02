package io.teamscala.java.sample.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.teamscala.java.core.format.EnumConverterFactory;
import io.teamscala.java.core.format.NumberConverterFactory;
import io.teamscala.java.jpa.format.JpaEntityConverter;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;

import javax.inject.Inject;

/**
 * Default formatter registrar.
 */
public class DefaultFormatterRegistrar implements FormatterRegistrar {

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConverterFactory());
        registry.addConverterFactory(new NumberConverterFactory());

        DateFormatter dateFormatter = new DateFormatter("yyyy-MM-dd");
        dateFormatter.setLenient(true);
        registry.addFormatter(dateFormatter);

        registry.addConverter(new JpaEntityConverter((ConversionService) registry, objectMapper));
    }
}
