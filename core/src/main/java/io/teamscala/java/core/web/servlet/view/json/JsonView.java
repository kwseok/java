package io.teamscala.java.core.web.servlet.view.json;

import io.teamscala.java.core.json.jsonlib.util.JSONUtils;
import io.teamscala.java.core.util.ClassUtils;
import io.teamscala.java.core.web.util.WebUtils;
import net.sf.json.JSON;
import net.sf.json.JSONNull;
import net.sf.json.JsonConfig;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * A View that renders its model as a JSON object.
 * 
 */
public class JsonView extends AbstractView {

	/** Default content type. Overridable as bean property. */
	private static final String DEFAULT_CONTENT_TYPE = "application/json";

	/** Default content type for MSIE. */
	public static final String DEFAULT_MSIE_CONTENT_TYPE = "text/plain";


	/** Json configuration */
	private JsonConfig jsonConfig = JSONUtils.defaultConfig();

	private Set<String> modelKeys;

	private boolean extractValueFromSingleKeyModel = false;

	private boolean disableCaching = true;

	private String msieContentType = DEFAULT_MSIE_CONTENT_TYPE;


	public JsonView() {
		setContentType(DEFAULT_CONTENT_TYPE);
		setExposePathVariables(false);
	}


	public JsonConfig getJsonConfig() {
		return jsonConfig;
	}

	/**
	 * @return the group of properties to be excluded.
	 */
	public String[] isExcludedProperties() {
		return jsonConfig.getExcludes();
	}

	/**
	 * @return whether the JSONSerializer will ignore or not its internal property exclusions.
	 */
	public boolean isIgnoreDefaultExcludes() {
		return jsonConfig.isIgnoreDefaultExcludes();
	}

	/**
	 * @return the attributes in the model that should be rendered by this view.
	 */
	public Set<String> getModelKeys() {
		return this.modelKeys;
	}

	public String getMsieContentType() {
		return msieContentType;
	}

	public void setJsonConfig(JsonConfig jsonConfig) {
		this.jsonConfig = jsonConfig != null ? jsonConfig : JSONUtils.defaultConfig();
	}

    /**
     * @param excludedProperties Sets the group of properties to be excluded.
	 */
	public void setExcludedProperties(String[] excludedProperties) {
		jsonConfig.setExcludes(excludedProperties);
	}

	/**
     * @param ignoreDefaultExcludes Sets whether the JSONSerializer will ignore or
     *                              not its internal property exclusions.
	 */
	public void setIgnoreDefaultExcludes(boolean ignoreDefaultExcludes) {
		jsonConfig.setIgnoreDefaultExcludes(ignoreDefaultExcludes);
	}

	/**
     * @param modelKey Set the attribute in the model that should be rendered by this view.
     *                 When set, all other model attributes will be ignored.
	 */
	public void setModelKey(String modelKey) {
		this.modelKeys = Collections.singleton(modelKey);
	}

	/**
     * @param modelKeys Set the attributes in the model that should be rendered by this view.
     *                  When set, all other model attributes will be ignored.
	 */
	public void setModelKeys(Set<String> modelKeys) {
		this.modelKeys = modelKeys;
	}

	/**
     * @param extractValueFromSingleKeyModel Set whether to serialize models containing a single attribute as a map or
     *                                       whether to extract the single value from the model and serialize it directly.
     *                                       Default is {@code false}.
	 */
	public void setExtractValueFromSingleKeyModel(boolean extractValueFromSingleKeyModel) {
		this.extractValueFromSingleKeyModel = extractValueFromSingleKeyModel;
	}

	/**
     * @param disableCaching  Disables caching of the generated JSON.
     *                        Default is {@code true},
     *                        which will prevent the client from caching the generated JSON.
	 */
	public void setDisableCaching(boolean disableCaching) {
		this.disableCaching = disableCaching;
	}

	/**
     * @param msieContentType Set the content type for MSIE.
     *                        Default is {@value #DEFAULT_MSIE_CONTENT_TYPE}.
	 */
	public void setMsieContentType(String msieContentType) {
		this.msieContentType = msieContentType;
	}


	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		if (WebUtils.isAjaxAndMsieAndMultipart(request))
			response.setContentType(getMsieContentType());
		else
			response.setContentType(getContentType());

		if (this.disableCaching) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addDateHeader("Expires", 1L);
		}
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonConfig jsonConfig = findJsonConfig(model);
		Object value = filterModel(model);
		if (isSimpleValue(value))
			response.getWriter().write(createJSONString(value, request, response));
		else
			createJSON(value, jsonConfig, request, response).write(response.getWriter());
	}

	protected JsonConfig findJsonConfig(Map<String, Object> model) {
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof JsonConfig) {
				model.remove(entry.getKey());
				return (JsonConfig) value;
			}
		}
		return jsonConfig;
	}

	protected Object filterModel(Map<String, Object> model) {
		Map<String, Object> result = new HashMap<>(model.size());
		Set<String> renderedAttributes = (!CollectionUtils.isEmpty(this.modelKeys) ? this.modelKeys : model.keySet());
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (!(value instanceof BindingResult)
					&& !(value instanceof Throwable)
					&& renderedAttributes.contains(key)) result.put(key, value);
		}
		return (this.extractValueFromSingleKeyModel && result.size() == 1 ? result.values().iterator().next() : result);
	}

	protected boolean isSimpleValue(Object value) {
		return (value == null) || ClassUtils.isSimpleType(value.getClass());
	}

	/**
	 * Creates a String for the simple value.
     *
     * @param value the value.
     * @param request the request.
     * @param response the response.
     * @return the json string.
     */
	protected String createJSONString(Object value, HttpServletRequest request, HttpServletResponse response) {
		if (value == null) return JSONNull.getInstance().toString();
		if (value instanceof CharSequence) return net.sf.json.util.JSONUtils.quote(value.toString());
		if (value instanceof Date) return String.valueOf(((Date) value).getTime());
		if (value instanceof Class) return ((Class<?>) value).getName();

		return String.valueOf(value);
	}

	/**
     * Creates a JSON [JSONObject,JSONArray,JSONNUll] from the value.
     *
     * @param value the value.
     * @param jsonConfig the json config.
     * @param request the request.
     * @param response the response.
     * @return the {@link net.sf.json.JSON}
     */
	protected JSON createJSON(Object value, JsonConfig jsonConfig, HttpServletRequest request, HttpServletResponse response) {
		return JSONUtils.toJSON(value, jsonConfig);
	}
}