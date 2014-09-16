package io.teamscala.java.sample.config;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import static io.teamscala.java.sample.misc.Globals.ACTIVE_PROFILE;

public class AppInitializer extends AbstractSecurityWebApplicationInitializer {

    @Override
    protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
        // Params
        servletContext.setInitParameter("defaultHtmlEscape", "true");
        servletContext.setInitParameter("log4jConfigLocation", "classpath:log4j-" + ACTIVE_PROFILE + ".properties");
        servletContext.setInitParameter("spring.profiles.active", ACTIVE_PROFILE);

        // Listeners
        servletContext.addListener(Log4jConfigListener.class);

        // Application Context
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(io.teamscala.java.sample.config.ApplicationConfig.class);
        servletContext.addListener(new ContextLoaderListener(applicationContext));

        // Dispatcher Serlvet
        addDispatcherServlet(servletContext);

        // Filters
        addCharacterEncodingFilter(servletContext);
        addHiddenHttpMethodFilter(servletContext);
        addOpenEntityManagerInViewFilter(servletContext);
        addRequestContextFilter(servletContext);
    }

    private void addDispatcherServlet(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(io.teamscala.java.sample.config.MvcConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(applicationContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    private void addCharacterEncodingFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic filter = servletContext.addFilter("CharacterEncodingFilter", CharacterEncodingFilter.class);
        filter.setInitParameter("encoding", "UTF-8");
        filter.setInitParameter("forceEncoding", "true");
        filter.addMappingForUrlPatterns(null, false, "/*");
    }

    private void addHiddenHttpMethodFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic filter = servletContext.addFilter("HttpMethodFilter", HiddenHttpMethodFilter.class);
        filter.addMappingForUrlPatterns(null, false, "/*");
    }

    private void addOpenEntityManagerInViewFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic filter = servletContext.addFilter("OpenEntityManagerInViewFilter", OpenEntityManagerInViewFilter.class);
        filter.addMappingForUrlPatterns(null, false, "/*");
    }

    private void addRequestContextFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic filter = servletContext.addFilter("RequestContextFilter", RequestContextFilter.class);
        filter.setInitParameter("threadContextInheritable", "true");
        filter.addMappingForUrlPatterns(null, false, "/*");
    }

}
