package io.teamscala.java.sample.config;

import com.typesafe.config.Config;
import io.teamscala.java.sample.FQBeanNameGenerator;
import io.teamscala.java.sample.misc.Configs;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;

import static org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(
    basePackages = "com.b2pharm.glas",
    nameGenerator = FQBeanNameGenerator.class,
    excludeFilters = {
        @Filter(value = Controller.class),
        @Filter(value = MvcConfig.class, type = FilterType.ASSIGNABLE_TYPE)
    }
)
@EnableSpringConfigured
@EnableLoadTimeWeaving
@EnableAspectJAutoProxy
@ImportResource("classpath:META-INF/spring/context-security.xml")
public class ApplicationConfig {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:META-INF/i18n/messages");
        return messageSource;
    }

    @Bean
    public static Config config() {
        return Configs.current;
    }

}
