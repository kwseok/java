package io.teamscala.java.sample.web.interceptors;

import io.teamscala.java.core.util.ClassUtils;
import io.teamscala.java.core.util.StringUtils;
import io.teamscala.java.core.web.servlet.UrlPatternMatchingInterceptorAdapter;
import io.teamscala.java.sample.misc.Configs;
import io.teamscala.java.sample.security.util.SecurityUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;


/**
 * User Agent Interceptor.
 */
public class UserAgentInterceptor extends UrlPatternMatchingInterceptorAdapter {

    public static final long APPLICATION_START_TIME = System.currentTimeMillis();
    public static final Map<String, Map<String, Object>> ENUMS = new HashMap<>();

    public static final String ENUM_OF_ORDINAL_ELEMENT_NAME = "ofOrdinal";
    public static final String ENUM_OF_STRING_ELEMENT_NAME = "ofString";

    public static final String[] ENUM_SCAN_PACKAGES = {
        "com.b2pharm.glas.misc",
        "com.b2pharm.glas.models"
    };

    static {
        //Scan Enum Types
        BeanDefinitionRegistry bdr = new SimpleBeanDefinitionRegistry();
        ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(bdr);
        s.resetFilters(false);
        s.addIncludeFilter(new AssignableTypeFilter(Enum.class));
        s.scan(ENUM_SCAN_PACKAGES);
        for (String beanName : bdr.getBeanDefinitionNames()) {
            BeanDefinition bean = bdr.getBeanDefinition(beanName);
            try {
                Class<?> enumClass = ClassUtils.getClass(bean.getBeanClassName());
                if (Enum.class.isAssignableFrom(enumClass)) {
                    Enum<?>[] enumConstants = (Enum<?>[]) enumClass.getEnumConstants();
                    if (ArrayUtils.isNotEmpty(enumConstants)) {
                        Map<String, Object> enumValues = new TreeMap<>();
                        List<Map<String, Object>> ordinalValues = new ArrayList<>();
                        Map<String, Object> stringValues = new TreeMap<>();

                        ENUMS.put(StringUtils.capitalize(beanName), enumValues);
                        enumValues.put(ENUM_OF_ORDINAL_ELEMENT_NAME, ordinalValues);
                        enumValues.put(ENUM_OF_STRING_ELEMENT_NAME, stringValues);

                        for (Enum<?> e : enumConstants) {
                            BeanWrapper enumWrapper = new BeanWrapperImpl(e);
                            PropertyDescriptor[] properties = enumWrapper.getPropertyDescriptors();
                            Map<String, Object> value = new HashMap<>(1 + properties.length);

                            for (PropertyDescriptor property : properties) {
                                Method readMethod = property.getReadMethod();
                                if (readMethod != null && readMethod.getDeclaringClass().equals(e.getClass())) {
                                    value.put(property.getName(), enumWrapper.getPropertyValue(property.getName()));
                                }
                            }
                            stringValues.put(e.name(), value);

                            Map<String, Object> valueForOrdinal = new HashMap<>(value);
                            valueForOrdinal.put("name", e.name());
                            ordinalValues.add(valueForOrdinal);
                        }
                    }
                }
            } catch (ClassNotFoundException | LinkageError e) {
                e.printStackTrace();
            }
        }
    }

    public static final String APPLICATION_START_TIME_ATTRIBUTE_NAME = "APPLICATION_START_TIME";
    public static final String CONTEXT_PATH_ATTRIBUTE_NAME = "CONTEXT_PATH";
    public static final String CURRENT_USER_ATTRIBUTE_NAME = "CURRENT_USER";
    //public static final String GROUP_CODES_ATTRIBUTE_NAME = "GROUP_CODES";
    //public static final String REFER_CODES_ATTRIBUTE_NAME = "REFER_CODES";
    public static final String ENUMS_ATTRIBUTE_NAME = "ENUMS";
    public static final String CONFIG_ATTRIBUTE_NAME = "CONFIG";

    @Override
    protected boolean preHandleInternal(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 어플리케이션 시작 시간
        request.setAttribute(APPLICATION_START_TIME_ATTRIBUTE_NAME, APPLICATION_START_TIME);
        // CONTEXT PATH
        request.setAttribute(CONTEXT_PATH_ATTRIBUTE_NAME, request.getContextPath());
        // 현재 사용자
        request.setAttribute(CURRENT_USER_ATTRIBUTE_NAME, SecurityUtils.getUser().orElse(null));
        // ENUM Types
        request.setAttribute(ENUMS_ATTRIBUTE_NAME, ENUMS);
        // Config
        request.setAttribute(CONFIG_ATTRIBUTE_NAME, Configs.current);

        return true;
    }
}
