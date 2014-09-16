package io.teamscala.java.sample.config;

import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import io.teamscala.java.core.format.EnumConverterFactory;
import io.teamscala.java.core.format.NumberConverterFactory;
import io.teamscala.java.core.oxm.xstream.XStreamMarshaller;
import io.teamscala.java.core.web.method.support.PageableArgumentResolver;
import io.teamscala.java.core.web.servlet.handler.*;
import io.teamscala.java.core.web.servlet.view.json.MappingJackson2JsonView;
import io.teamscala.java.jpa.format.JpaEntityConverter;
import io.teamscala.java.jpa.json.jackson.HibernateAwareObjectMapper;
import io.teamscala.java.jpa.web.method.support.JpaEntityAttributeArgumentResolver;
import io.teamscala.java.sample.FQBeanNameGenerator;
import io.teamscala.java.sample.web.interceptors.UserAgentInterceptor;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.xml.MarshallingView;

import java.util.*;

import static javax.servlet.http.HttpServletResponse.*;

@Configuration
@ComponentScan(
    basePackages = "com.b2pharm.glas.web.controllers",
    useDefaultFilters = true,
    nameGenerator = FQBeanNameGenerator.class,
    includeFilters = {@ComponentScan.Filter(Controller.class)}
)
@EnableTransactionManagement(mode = AdviceMode.PROXY)
public class MvcConfig extends WebMvcConfigurationSupport {

    @Bean
    public HibernateAwareObjectMapper objectMapper() {
        HibernateAwareObjectMapper objectMapper = new HibernateAwareObjectMapper();
        objectMapper.enable(Hibernate4Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        return objectMapper;
    }

    @Bean
    public XStreamMarshaller xStreamMarshaller() {
        return new XStreamMarshaller();
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasenames(
            "classpath:META-INF/i18n/messages",
            "WEB-INF/i18n/messages",
            "WEB-INF/i18n/application"
        );
        return messageSource;
    }

    @Bean
    public CookieLocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }

    @Bean
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

    @Bean
    public SimpleHandlerExceptionResolver simpleHandlerExceptionResolver() {
        SimpleExceptionHandler resourceNotFoundHandler = new SimpleExceptionHandler(SC_NOT_FOUND);
        resourceNotFoundHandler.setErrorViewName("resourceNotFound");
        resourceNotFoundHandler.setErrorMessageCode("error.resourceNotFound");
        resourceNotFoundHandler.setExceptions(Arrays.asList(
            org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException.class,
            org.springframework.web.bind.MissingServletRequestParameterException.class
        ));

        SimpleExceptionHandler uploadExceptionHandler = new SimpleExceptionHandler(SC_BAD_REQUEST);
        uploadExceptionHandler.setErrorViewName("badRequest");
        uploadExceptionHandler.setErrorMessageCode("error.upload.sizeExceeded");
        uploadExceptionHandler.setErrorMessageArguments(new String[]{"maxUploadSize"});
        uploadExceptionHandler.setException(org.springframework.web.multipart.MaxUploadSizeExceededException.class);

        BindExceptionHandler bindExceptionHandler = new BindExceptionHandler();
        bindExceptionHandler.setErrorViewName("badRequest");

        HttpExceptionHandler httpExceptionHandler = new HttpExceptionHandler();
        Map<Integer, String> errorViewMappings = new HashMap<>();
        errorViewMappings.put(SC_BAD_REQUEST, "badRequest");
        errorViewMappings.put(SC_FORBIDDEN, "accessDenied");
        errorViewMappings.put(SC_NOT_FOUND, "resourceNotFound");
        httpExceptionHandler.setErrorViewMappings(errorViewMappings);

        List<ExceptionHandler> exceptionHandlers = new ArrayList<>();
        exceptionHandlers.add(resourceNotFoundHandler);
        exceptionHandlers.add(uploadExceptionHandler);
        exceptionHandlers.add(bindExceptionHandler);
        exceptionHandlers.add(httpExceptionHandler);

        SimpleHandlerExceptionResolver exceptionResolver = new SimpleHandlerExceptionResolver();
        exceptionResolver.setOrder(2);
        exceptionResolver.setMessageSource(messageSource());
        exceptionResolver.setDefaultErrorViewName("uncaughtException");
        exceptionResolver.setAjaxErrorView(jsonView());
        exceptionResolver.setExceptionHandlers(exceptionHandlers);
        return exceptionResolver;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public MappingJackson2JsonView jsonView() {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        jsonView.setObjectMapper(objectMapper());
        return jsonView;
    }

    @Bean
    public MappingJackson2JsonView jsonViewResult() {
        MappingJackson2JsonView jsonViewResult = new MappingJackson2JsonView();
        jsonViewResult.setObjectMapper(objectMapper());
        jsonViewResult.setModelKey("result");
        jsonViewResult.setExtractValueFromSingleKeyModel(true);
        return jsonViewResult;
    }

    @Bean
    public MarshallingView xmlView() {
        return new MarshallingView(xStreamMarshaller());
    }

    @Bean
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver() {
        Map<String, MediaType> mediaTypes = new HashMap<>();
        mediaTypes.put("json", MediaType.APPLICATION_JSON);
        mediaTypes.put("xml", MediaType.APPLICATION_XML);
        mediaTypes.put("html", MediaType.TEXT_HTML);

        ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setOrder(1);
        viewResolver.setContentNegotiationManager(
            new ContentNegotiationManager(
                new PathExtensionContentNegotiationStrategy(mediaTypes)
            )
        );
        viewResolver.setDefaultViews(Arrays.asList(jsonView(), xmlView()));
        return viewResolver;
    }

    @Bean
    public BeanNameViewResolver beanNameViewResolver() {
        BeanNameViewResolver viewResolver = new BeanNameViewResolver();
        viewResolver.setOrder(2);
        return viewResolver;
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setOrder(4);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(exceptionHandlerExceptionResolver());
        exceptionResolvers.add(simpleHandlerExceptionResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper());
        converters.add(jsonConverter);

        MarshallingHttpMessageConverter marshallingConverter = new MarshallingHttpMessageConverter();
        marshallingConverter.setMarshaller(xStreamMarshaller());
        converters.add(marshallingConverter);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateFormatter df = new DateFormatter("yyyy-MM-dd");
        df.setLenient(true);
        registry.addFormatter(df);
        registry.addConverterFactory(new EnumConverterFactory());
        registry.addConverterFactory(new NumberConverterFactory());
        registry.addConverter(new JpaEntityConverter((ConversionService) registry, objectMapper()));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableArgumentResolver());
        argumentResolvers.add(new JpaEntityAttributeArgumentResolver(mvcConversionService()));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/", "classpath:/META-INF/web-resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/META-INF/i18n/");
        //registry.addResourceHandler("/admin/engine/summary/diff/**").addResourceLocations("classpath:/META-INF/web-resources/daisydiff/");
        //registry.addResourceHandler("/admin/engine/detail/diff/**").addResourceLocations("classpath:/META-INF/web-resources/daisydiff/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
        registry.addInterceptor(new UserAgentInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/resources/**",
                "/assets/**",
                "/webjars/**"
            );
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/uncaughtException");
        registry.addViewController("/resourceNotFound");
        registry.addViewController("/badRequest");
        registry.addViewController("/accessDenied");
        registry.addViewController("/**/*.html");
    }
}
