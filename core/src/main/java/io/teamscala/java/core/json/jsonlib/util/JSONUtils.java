package io.teamscala.java.core.json.jsonlib.util;

import io.teamscala.java.core.json.jsonlib.configurators.JsonConfigurator;
import io.teamscala.java.core.json.jsonlib.processors.JsDateJsonValueProcessor;
import io.teamscala.java.core.json.jsonlib.processors.JsonPreprocessor;
import io.teamscala.java.core.json.jsonlib.processors.ProcessorMatcherSupport;
import io.teamscala.java.core.util.ClassUtils;
import net.sf.json.*;
import net.sf.json.processors.JsonBeanProcessorMatcher;
import net.sf.json.processors.JsonValueProcessorMatcher;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.commons.beanutils.DynaBean;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import static org.springframework.beans.BeanUtils.instantiateClass;

/**
 * JSON utility class.
 */
public abstract class JSONUtils {

    private static final String SERVICES_ROOT = "META-INF/services/";
    private static final List<JsonConfigurator> CONFIGURATORS = new ArrayList<>();
    private static final List<JsonPreprocessor<?>> PREPROCESSORS = new ArrayList<>();

    static {
        final ClassLoader classLoader = JSONUtils.class.getClassLoader();

        // Initialize Configurators
        try {
            Enumeration<URL> resources = classLoader.getResources(SERVICES_ROOT + JsonConfigurator.class.getName());
            while (resources.hasMoreElements()) {
                InputStream is = resources.nextElement().openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if ((line = line.trim()).length() > 0)
                            registerJsonConfigurator((JsonConfigurator) instantiateClass(ClassUtils.getClass(classLoader, line)));
                    }
                } finally {
                    try {
                        is.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new JSONException(e);
        }

        // Initialize Preprocessors
        try {
            Enumeration<URL> resources = classLoader.getResources(SERVICES_ROOT + JsonPreprocessor.class.getName());
            while (resources.hasMoreElements()) {
                InputStream is = resources.nextElement().openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if ((line = line.trim()).length() > 0)
                            registerJsonPreprocessor((JsonPreprocessor<?>) instantiateClass(ClassUtils.getClass(classLoader, line)));
                    }
                } finally {
                    try {
                        is.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    public static void registerJsonConfigurator(JsonConfigurator jsonConfigurator) {
        Assert.notNull(jsonConfigurator, "JsonConfigurator must not be null");
        CONFIGURATORS.add(jsonConfigurator);
    }

    public static void registerJsonPreprocessor(JsonPreprocessor<?> jsonPreprocessor) {
        Assert.notNull(jsonPreprocessor, "JsonPreprocessor must not be null");
        PREPROCESSORS.add(jsonPreprocessor);
    }


    private static final ProcessorMatcherSupport PROCESSOR_MATCHER_SUPPORT = new ProcessorMatcherSupport();

    /**
     * @return The default json config.
     */
    public static JsonConfig defaultConfig() {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        jsonConfig.setIgnoreTransientFields(true);
        jsonConfig.addIgnoreFieldAnnotation(java.beans.Transient.class);
        jsonConfig.addIgnoreFieldAnnotation(io.teamscala.java.core.json.jsonlib.annotations.JsonIgnore.class);
        jsonConfig.addIgnoreFieldAnnotation(com.fasterxml.jackson.annotation.JsonIgnore.class);
        jsonConfig.setJsonBeanProcessorMatcher(new JsonBeanProcessorMatcher() {
            @SuppressWarnings("rawtypes")
            @Override
            public Object getMatch(Class target, Set matches) {
                Object match = PROCESSOR_MATCHER_SUPPORT.getMatch(target, matches);
                if (match == null) match = DEFAULT.getMatch(target, matches);
                return match;
            }
        });
        jsonConfig.setJsonValueProcessorMatcher(new JsonValueProcessorMatcher() {
            @SuppressWarnings("rawtypes")
            @Override
            public Object getMatch(Class target, Set matches) {
                Object match = PROCESSOR_MATCHER_SUPPORT.getMatch(target, matches);
                if (match == null) match = DEFAULT.getMatch(target, matches);
                return match;
            }
        });
        jsonConfig.registerJsonValueProcessor(Date.class, new JsDateJsonValueProcessor());

        for (JsonConfigurator configurator : CONFIGURATORS) configurator.configure(jsonConfig);
        return jsonConfig;
    }

    /**
     * Preprocess.
     *
     * @param target     the target object.
     * @param jsonConfig the json config.
     * @return the processed object
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object preprocess(Object target, JsonConfig jsonConfig) {
        if (target != null) {
            for (JsonPreprocessor preprocessor : PREPROCESSORS) {
                Class<?> typeArgument = ResolvableType
                    .forClass(preprocessor.getClass())
                    .as(JsonPreprocessor.class)
                    .resolveGeneric(0);

                if (typeArgument != null && typeArgument.isAssignableFrom(target.getClass())) {
                    target = preprocessor.process(target, jsonConfig);
                    if (target instanceof JSON) break;
                }
            }
        }
        return target;
    }

    /**
     * 주어진 객체를 JSON 형식의 문자열로 반환한다.
     *
     * @param target the target object
     * @return JSON 형식의 문자열
     */
    public static String toJSONString(Object target) {
        return toJSON(target).toString();
    }

    /**
     * 주어진 객체를 JSON 형식의 문자열로 반환한다.
     *
     * @param target      the target object
     * @param prettyPrint the pretty print
     * @return {@link JSON}
     */
    public static String toJSONString(Object target, boolean prettyPrint) {
        return toJSON(target).toString(prettyPrint ? 2 : 0);
    }

    /**
     * 주어진 객체를 JSON 형식의 문자열로 반환한다.
     *
     * @param target       the target object
     * @param indentFactor The number of spaces to add to each level of indentation.
     * @return {@link JSON}
     */
    public static String toJSONString(Object target, int indentFactor) {
        return toJSON(target).toString(indentFactor);
    }

    /**
     * 주어진 객체를 JSON 형식의 문자열로 반환한다.
     *
     * @param target       the target object
     * @param indentFactor The number of spaces to add to each level of indentation.
     * @param indent       The indentation of the top level.
     * @return {@link JSON}
     */
    public static String toJSONString(Object target, int indentFactor, int indent) {
        return toJSON(target).toString(indentFactor, indent);
    }

    /**
     * 주어진 객체를 {@link JSON} 객체로 반환한다.
     *
     * @param target the target object
     * @return {@link JSON}
     */
    public static JSON toJSON(Object target) {
        return toJSON(target, defaultConfig());
    }

    /**
     * 주어진 객체를 {@link JSON} 객체로 반환한다.
     *
     * @param target     the target object
     * @param jsonConfig the json config
     * @return {@link JSON}
     */
    public static JSON toJSON(Object target, JsonConfig jsonConfig) {
        return JSONSerializer.toJSON(preprocess(target, jsonConfig), jsonConfig);
    }

    /**
     * 주어진 객체를 {@link JSONObject} 객체로 반환한다.
     *
     * @param target the target object
     * @return {@link JSONObject}
     */
    public static JSONObject toJSONObject(Object target) {
        return toJSONObject(target, defaultConfig());
    }

    /**
     * 주어진 객체를 {@link JSONObject} 객체로 반환한다.
     *
     * @param target     the target object
     * @param jsonConfig the json config
     * @return {@link JSONObject}
     */
    public static JSONObject toJSONObject(Object target, JsonConfig jsonConfig) {
        return JSONObject.fromObject(preprocess(target, jsonConfig), jsonConfig);
    }

    /**
     * 주어진 객체를 {@link JSONArray} 객체로 반환한다.
     *
     * @param target the target object
     * @return {@link JSONArray}
     */
    public static JSONArray toJSONArray(Object target) {
        return toJSONArray(target, defaultConfig());
    }

    /**
     * 주어진 객체를 {@link JSONArray} 객체로 반환한다.
     *
     * @param target     the target object
     * @param jsonConfig the json config
     * @return {@link JSONArray}
     */
    public static JSONArray toJSONArray(Object target, JsonConfig jsonConfig) {
        return JSONArray.fromObject(preprocess(target, jsonConfig), jsonConfig);
    }

    public static DynaBean toBean(JSONObject jsonObject) {
        return (DynaBean) JSONObject.toBean(jsonObject);
    }

    public static <T> T toBean(JSONObject jsonObject, Class<T> beanClass) {
        JsonConfig jsonConfig = defaultConfig();
        jsonConfig.setRootClass(beanClass);
        return toBean(jsonObject, jsonConfig);
    }

    public static <T> T toBean(JSONObject jsonObject, Class<T> beanClass, Map<String, Class<?>> classMap) {
        JsonConfig jsonConfig = defaultConfig();
        jsonConfig.setRootClass(beanClass);
        jsonConfig.setClassMap(classMap);
        return toBean(jsonObject, jsonConfig);
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(JSONObject jsonObject, JsonConfig jsonConfig) {
        return (T) JSONObject.toBean(jsonObject, jsonConfig);
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(JSONObject jsonObject, T root, JsonConfig jsonConfig) {
        return (T) JSONObject.toBean(jsonObject, root, jsonConfig);
    }

    public static boolean isArray(String json) {
        if (json != null) {
            json = json.trim();
            return json.length() >= 2
                && '[' == json.charAt(0)
                && ']' == json.charAt(json.length() - 1);
        }
        return false;
    }

    public static Object[] toArray(JSONArray jsonArray) {
        return toArray(jsonArray, defaultConfig());
    }

    public static <T> T[] toArray(JSONArray jsonArray, Class<T> objectClass) {
        JsonConfig jsonConfig = defaultConfig();
        jsonConfig.setRootClass(objectClass);
        return toArray(jsonArray, jsonConfig);
    }

    public static <T> T[] toArray(JSONArray jsonArray, Class<T> objectClass, Map<String, Class<?>> classMap) {
        JsonConfig jsonConfig = defaultConfig();
        jsonConfig.setRootClass(objectClass);
        jsonConfig.setClassMap(classMap);
        return toArray(jsonArray, jsonConfig);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(JSONArray jsonArray, JsonConfig jsonConfig) {
        return (T[]) JSONArray.toArray(jsonArray, jsonConfig);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(JSONArray jsonArray, T root, JsonConfig jsonConfig) {
        return (T[]) JSONArray.toArray(jsonArray, root, jsonConfig);
    }

    public static <T> Collection<T> toCollection(JSONArray jsonArray) {
        return toCollection(jsonArray, defaultConfig());
    }

    public static <T> Collection<T> toCollection(JSONArray jsonArray, Class<T> objectClass) {
        JsonConfig jsonConfig = defaultConfig();
        jsonConfig.setRootClass(objectClass);
        return toCollection(jsonArray, jsonConfig);
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> toCollection(JSONArray jsonArray, JsonConfig jsonConfig) {
        return JSONArray.toCollection(jsonArray, jsonConfig);
    }

    public static <T> List<T> toList(JSONArray jsonArray, T root) {
        return toList(jsonArray, root, defaultConfig());
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(JSONArray jsonArray, T root, JsonConfig jsonConfig) {
        return JSONArray.toList(jsonArray, root, jsonConfig);
    }

}