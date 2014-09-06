package io.teamscala.java.core.web.method.support;

import io.teamscala.java.core.util.ClassUtils;
import io.teamscala.java.core.util.StringUtils;
import io.teamscala.java.core.web.method.annotation.BindFields;
import io.teamscala.java.core.web.method.annotation.BindStrategy;
import io.teamscala.java.core.web.method.annotation.BindType;
import io.teamscala.java.core.web.method.annotation.EntityAttribute;
import io.teamscala.java.core.web.method.bind.groups.Default;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * Abstract entity attribute argument resolver.
 */
public abstract class AbstractEntityAttributeArgumentResolver implements HandlerMethodArgumentResolver, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractEntityAttributeArgumentResolver.class);

    public static final Class<?>[] DEFAULT_BIND_GROUPS = new Class<?>[]{Default.class};
    public static final String[] DEFAULT_BIND_NESTED_PATHS = new String[]{".*", "[*]*"};

    private final ConversionService conversionService;
    private String[] bindNestedPaths = DEFAULT_BIND_NESTED_PATHS;

    public AbstractEntityAttributeArgumentResolver(ConversionService conversionService) {
        Assert.notNull(conversionService, "'conversionService' must not be null");
        this.conversionService = conversionService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (conversionService == null) throw new IllegalArgumentException("'conversionService' is required");
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public String[] getBindNestedPaths() {
        return bindNestedPaths;
    }

    public void setBindNestedPaths(String[] bindNestedPaths) {
        this.bindNestedPaths = defaultIfNull(bindNestedPaths, DEFAULT_BIND_NESTED_PATHS);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(EntityAttribute.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        String attributeName = getAttributeNameForParameter(parameter);
        Object identifier = getIdentifier(parameter, webRequest, binderFactory);
        Object target;
        boolean isNewEntity = true;

        if (identifier != null) {
            target = findEntity(identifier, parameter);
            LOG.debug("Entity found: " + target);
            if (target == null && !isRejectNotFound(parameter))
                target = createAttribute(attributeName, parameter, webRequest, binderFactory);
            else isNewEntity = false;
        } else target = createAttribute(attributeName, parameter, webRequest, binderFactory);

        WebDataBinder binder = binderFactory.createBinder(webRequest, target, attributeName);
        if (target == null) {
            binder.getBindingResult()
                .reject(getNotFoundMessage(parameter), new Object[]{identifier},
                    "Entity not found #" + identifier);
        } else {
            if (isDirectFieldAccess(parameter))
                binder.initDirectFieldAccess();
            else
                binder.initBeanPropertyAccess();

            ServletRequest servletRequest = (ServletRequest) webRequest.getNativeRequest();
            PropertyValues propertyValues = new ServletRequestParameterPropertyValues(servletRequest,
                getPrefixForParameter(parameter));

            binder.setAllowedFields(getAllowedFields(parameter, isNewEntity));
            binder.setDisallowedFields(getDisallowedFields(parameter, isNewEntity));

            if (LOG.isDebugEnabled()) {
                LOG.debug("Allowed fields for binding : " + ArrayUtils.toString(binder.getAllowedFields()));
                LOG.debug("Disallowed fields for binding : " + ArrayUtils.toString(binder.getDisallowedFields()));
            }

            BindInfo bindInfo = BindInfoFactory.get(parameter.getParameterType());
            for (Method m : bindInfo.getPreBindEvents()) m.invoke(target);
            binder.bind(propertyValues);
            for (Method m : bindInfo.getPostBindEvents()) m.invoke(target);

            validateIfApplicable(binder, parameter);
        }

        if (binder.getBindingResult().hasErrors()) {
            if (isBindExceptionRequired(binder, parameter)) {
                throw new BindException(binder.getBindingResult());
            }
        }

        // Add resolved attribute and BindingResult at the end of the model

        Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);

        return binder.getTarget();
    }

    /**
     * Get the identifier.
     *
     * @param parameter     the method parameter.
     * @param webRequest    the web request.
     * @param binderFactory the binder factory.
     * @return the identifier.
     * @throws Exception {@link java.lang.Exception}
     */
    protected abstract Object getIdentifier(MethodParameter parameter, NativeWebRequest webRequest,
                                            WebDataBinderFactory binderFactory) throws Exception;

    /**
     * Find the entity.
     *
     * @param identifier the identifier.
     * @param parameter  the method parameter.
     * @return the entity.
     */
    protected abstract Object findEntity(Object identifier, MethodParameter parameter);

    protected String getAttributeNameForParameter(MethodParameter parameter) {
        EntityAttribute annot = parameter.getParameterAnnotation(EntityAttribute.class);
        return (annot != null && annot.value().length() > 0) ? annot.value() : parameter.getParameterName();
    }

    protected String getPrefixForParameter(MethodParameter parameter) {
        EntityAttribute annot = parameter.getParameterAnnotation(EntityAttribute.class);
        return (annot != null && annot.prefix().length() > 0) ? annot.prefix() : null;
    }

    protected boolean isRejectNotFound(MethodParameter parameter) {
        EntityAttribute annot = parameter.getParameterAnnotation(EntityAttribute.class);
        return (annot == null) || annot.rejectIfNotFound();
    }

    protected boolean isDirectFieldAccess(MethodParameter parameter) {
        EntityAttribute annot = parameter.getParameterAnnotation(EntityAttribute.class);
        return (annot != null) && annot.directFieldAccess();
    }

    protected String getNotFoundMessage(MethodParameter parameter) {
        EntityAttribute annot = parameter.getParameterAnnotation(EntityAttribute.class);
        return (annot != null) ? annot.notFoundMessage() : "";
    }

    protected String[] getAllowedFields(MethodParameter parameter, boolean isNewEntity) throws ExecutionException {
        EntityAttribute annot = parameter.getParameterAnnotation(EntityAttribute.class);
        if (annot == null) return ArrayUtils.EMPTY_STRING_ARRAY;

        Set<String> allowedFields = new HashSet<>();
        for (BindFields bindFields : annot.includes()) {
            if (checkBindStrategy(bindFields.strategy(), isNewEntity)) {
                for (String fieldName : bindFields.value()) {
                    if (fieldName.length() > 0) allowedFields.add(fieldName);
                }
            }
        }

        allowedFields.addAll(getAnnotationDrivenBindFields(parameter, BindType.INCLUDE, isNewEntity));

        return allowedFields.toArray(new String[allowedFields.size()]);
    }

    protected String[] getDisallowedFields(MethodParameter parameter, boolean isNewEntity) throws ExecutionException {
        EntityAttribute annot = parameter.getParameterAnnotation(EntityAttribute.class);
        if (annot == null) return ArrayUtils.EMPTY_STRING_ARRAY;

        Set<String> disallowedFields = new HashSet<>();
        for (BindFields bindFields : annot.excludes()) {
            if (checkBindStrategy(bindFields.strategy(), isNewEntity)) {
                for (String fieldName : bindFields.value()) {
                    if (fieldName.length() > 0) disallowedFields.add(fieldName);
                }
            }
        }

        disallowedFields.addAll(getAnnotationDrivenBindFields(parameter, BindType.EXCLUDE, isNewEntity));

        return disallowedFields.toArray(new String[disallowedFields.size()]);
    }

    protected List<String> getAnnotationDrivenBindFields(MethodParameter parameter, BindType type, boolean isNewEntity) throws ExecutionException {
        EntityAttribute annot = parameter.getParameterAnnotation(EntityAttribute.class);
        if (annot == null || !annot.annotationDriven()) return Collections.emptyList();

        BindInfo bindInfo = BindInfoFactory.get(parameter.getParameterType());
        List<String> fields = new ArrayList<>(bindInfo.getDescriptions().size() * (bindNestedPaths.length + 1));

        for (BindDescription bd : bindInfo.getDescriptions()) {
            if (bd.getType() != type) continue;
            if (!checkBindStrategy(bd.getStrategy(), isNewEntity)) continue;
            if (!checkBindGroups(bd.getGroups(), annot.groups())) continue;

            String fieldName = bd.getTargetName();

            if (!bd.isPreventDefault()) fields.add(fieldName);

            if (!bd.isPreventDefaultNestedPaths() && !ClassUtils.isSimpleType(bd.getTargetType())) {
                for (String nestedPath : bindNestedPaths) fields.add(fieldName + nestedPath);
            }

            for (String nestedPath : bd.getNestedPaths()) {
                if (nestedPath.length() > 0) {
                    String infix = StringUtils.contains(".[", nestedPath.charAt(0)) ? "" : ".";
                    fields.add(fieldName + infix + nestedPath);
                }
            }
        }

        return fields;
    }

    protected boolean checkBindStrategy(BindStrategy strategy, boolean isNewEntity) {
        switch (strategy) {
            case ALWAYS: return true;
            case NEW: return isNewEntity;
            case MERGE: return !isNewEntity;
        }
        throw new IllegalArgumentException("Unsupported bind type : " + strategy);
    }

    protected boolean checkBindGroups(Class<?>[] provides, Class<?>[] requests) {
        if (ArrayUtils.isEmpty(provides)) provides = DEFAULT_BIND_GROUPS;
        if (ArrayUtils.isEmpty(requests)) requests = DEFAULT_BIND_GROUPS;

        if (provides == requests) return true;

        for (Class<?> request : requests) {
            if (ArrayUtils.contains(provides, request)) return true;
        }

        return false;
    }

    protected Object createAttribute(String attributeName, MethodParameter parameter, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        LOG.debug("Creating new entity");
        return BeanUtils.instantiateClass(parameter.getParameterType());
    }

    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation annot : annotations) {
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = AnnotationUtils.getValue(annot);
                binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
            }
        }
    }

    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
        return !(paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
    }

}