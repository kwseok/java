package io.teamscala.java.core.web.method.support;

import io.teamscala.java.core.data.PageRequest;
import io.teamscala.java.core.data.Pageable;
import io.teamscala.java.core.data.Sort;
import io.teamscala.java.core.data.Sort.Direction;
import io.teamscala.java.core.web.method.annotation.PageableDefaults;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletRequest;
import java.beans.PropertyEditorSupport;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Extracts paging information from web requests and thus allows injecting
 * {@link Pageable} instances into controller methods. Request properties to be
 * parsed can be configured. Default configuration uses request properties
 * beginning with {@link #DEFAULT_PREFIX}{@link #DEFAULT_SEPARATOR}.
 */
public class PageableArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Pageable DEFAULT_PAGE_REQUEST = new PageRequest();
    private static final String DEFAULT_PREFIX = "page";
    private static final String DEFAULT_SEPARATOR = ".";
    private static final String DEFAULT_INDEX_PROPERTY_NAME = "index";
    private static final String DEFAULT_SORT_DIRECTION_PROPERTY_NAME = "sort.dir";

    private Pageable fallbackPagable = DEFAULT_PAGE_REQUEST;
    private String prefix = DEFAULT_PREFIX;
    private String separator = DEFAULT_SEPARATOR;
    private String indexPropertyName = DEFAULT_INDEX_PROPERTY_NAME;
    private String sortDirectionPropertyName = DEFAULT_SORT_DIRECTION_PROPERTY_NAME;


    /**
     * Setter to configure a fallback instance of {@link Pageable} that is being
     * used to back missing parameters. Defaults to
     * {@link #DEFAULT_PAGE_REQUEST}.
     *
     * @param fallbackPagable the fallbackPagable to set
     */
    public void setFallbackPagable(Pageable fallbackPagable) {
        this.fallbackPagable = null == fallbackPagable ? DEFAULT_PAGE_REQUEST : fallbackPagable;
    }


    /**
     * Setter to configure the prefix of request parameters to be used to
     * retrieve paging information. Defaults to {@link #DEFAULT_PREFIX}.
     *
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = null == prefix ? DEFAULT_PREFIX : prefix;
    }


    /**
     * Setter to configure the separator between prefix and actual property
     * value. Defaults to {@link #DEFAULT_SEPARATOR}.
     *
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = null == separator ? DEFAULT_SEPARATOR : separator;
    }


    /**
     * Setter to configure the property name of page index. Defaults
     * to {@link #DEFAULT_INDEX_PROPERTY_NAME}
     *
     * @param indexPropertyName the property name of page index.
     */
    public void setIndexPropertyName(String indexPropertyName) {
        this.indexPropertyName = null == indexPropertyName ? DEFAULT_INDEX_PROPERTY_NAME : indexPropertyName;
    }


    /**
     * Setter to configure the property name of sort direction. Defaults
     * to {@link #DEFAULT_SORT_DIRECTION_PROPERTY_NAME}
     *
     * @param sortDirectionPropertyName the property name of sort direction.
     */
    public void setSortDirectionPropertyName(String sortDirectionPropertyName) {
        this.sortDirectionPropertyName = null == sortDirectionPropertyName ? DEFAULT_SORT_DIRECTION_PROPERTY_NAME : sortDirectionPropertyName;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Pageable.class);
    }


    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        assertPageableUniqueness(parameter);

        Pageable request = getDefaultFromAnnotationOrFallback(parameter);

        ServletRequest servletRequest = (ServletRequest) webRequest.getNativeRequest();

        MutablePropertyValues propertyValues = new ServletRequestParameterPropertyValues(servletRequest, getPrefix(parameter), separator);

        int correctionValue = 0;

        if (!propertyValues.contains(indexPropertyName)) {
            String index = servletRequest.getParameter(getPrefix(parameter));
            if (index != null) {
                correctionValue = 1;
                propertyValues.addPropertyValue(indexPropertyName, index);
            }
        }

        DataBinder binder = new ServletRequestDataBinder(request);

        binder.initDirectFieldAccess();
        binder.registerCustomEditor(Sort.class, new SortPropertyEditor(sortDirectionPropertyName, propertyValues));
        binder.bind(propertyValues);

        return new PageRequest((request.getIndex() > 0) ?
            request.getIndex() - correctionValue : 0, request.getSize(),
            request.getBoundSize(), request.getSort());
    }


    private Pageable getDefaultFromAnnotationOrFallback(MethodParameter methodParameter) {
        // search for PageableDefaults annotation
        for (Annotation annotation : methodParameter.getParameterAnnotations()) {
            if (annotation instanceof PageableDefaults) {
                PageableDefaults defaults = (PageableDefaults) annotation;
                // +1 is because we substract 1 later
                PageRequest defaultPageRequest = new PageRequest(defaults.index(), defaults.value(), defaults.boundSize());
                if (defaults.sort().length() > 0) {
                    defaultPageRequest.setSort(defaults.sort());
                }
                return defaultPageRequest;
            }
        }

        // Construct request with fallback request to ensure sensible
        // default values. Create fresh copy as Spring will manipulate the
        // instance under the covers
        return new PageRequest(fallbackPagable.getIndex(),
            fallbackPagable.getSize(), fallbackPagable.getBoundSize(),
            fallbackPagable.getSort());
    }


    /**
     * Resolves the prefix to use to bind properties from. Will prepend a
     * possible {@link Qualifier} if available or return the configured prefix
     * otherwise.
     *
     * @param parameter the method parameter.
     * @return the prefix.
     */
    private String getPrefix(MethodParameter parameter) {
        for (Annotation annotation : parameter.getParameterAnnotations()) {
            if (annotation instanceof Qualifier) {
                return ((Qualifier) annotation).value() + "_" + prefix;
            }
        }
        return prefix;
    }


    /**
     * Asserts uniqueness of all {@link Pageable} parameters of the method of
     * the given {@link MethodParameter}.
     *
     * @param parameter the method parameter.
     */
    private void assertPageableUniqueness(MethodParameter parameter) {
        Method method = parameter.getMethod();
        if (containsMoreThanOnePageableParameter(method)) {
            Annotation[][] annotations = method.getParameterAnnotations();
            assertQualifiersFor(method.getParameterTypes(), annotations);
        }
    }


    /**
     * Returns whether the given {@link Method} has more than one
     * {@link Pageable} parameter.
     *
     * @param method the method parameter.
     * @return true or false.
     */
    private boolean containsMoreThanOnePageableParameter(Method method) {
        boolean pageableFound = false;
        for (Class<?> type : method.getParameterTypes()) {
            if (pageableFound && type.equals(Pageable.class)) return true;
            if (type.equals(Pageable.class)) pageableFound = true;
        }
        return false;
    }


    /**
     * Asserts that every {@link Pageable} parameter of the given parameters
     * carries an {@link Qualifier} annotation to distinguish them from each
     * other.
     *
     * @param parameterTypes the parameter types.
     * @param annotations    the annotations.
     */
    private void assertQualifiersFor(Class<?>[] parameterTypes, Annotation[][] annotations) {
        Set<String> values = new HashSet<>();
        for (int i = 0; i < annotations.length; i++) {
            if (Pageable.class.equals(parameterTypes[i])) {
                Qualifier qualifier = findAnnotation(annotations[i]);

                if (null == qualifier)
                    throw new IllegalStateException(
                        "Ambiguous Pageable arguments in handler method. If you use multiple parameters of type Pageable you need to qualify them with @Qualifier");

                if (values.contains(qualifier.value()))
                    throw new IllegalStateException(
                        "Values of the user Qualifiers must be unique!");

                values.add(qualifier.value());
            }
        }
    }


    /**
     * Returns a {@link Qualifier} annotation from the given array of
     * {@link Annotation}s. Returns {@literal null} if the array does not
     * contain a {@link Qualifier} annotation.
     *
     * @param annotations the annotations.
     * @return the qualifier.
     */
    private Qualifier findAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Qualifier) {
                return (Qualifier) annotation;
            }
        }
        return null;
    }

    /**
     * {@link java.beans.PropertyEditor} to create {@link Sort} instances from
     * textual representations. The implementation interprets the string as a
     * comma separated list where the first entry is the sort direction (
     * {@code asc}, {@code desc}) followed by the properties to sort by.
     */
    private static class SortPropertyEditor extends PropertyEditorSupport {
        private final String directionProperty;
        private final PropertyValues values;


        /**
         * Creates a new {@link SortPropertyEditor}.
         *
         * @param directionProperty the direction property.
         * @param values            the values.
         */
        public SortPropertyEditor(String directionProperty, PropertyValues values) {
            this.directionProperty = directionProperty;
            this.values = values;
        }


        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            PropertyValue dir = values.getPropertyValue(directionProperty);
            if (dir == null) setValue(new Sort(text));
            else setValue(new Sort(Direction.fromString(dir.getValue().toString()), text));
        }
    }
}
