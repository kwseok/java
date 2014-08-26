package io.teamscala.java.core.web.util;

import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * URI Template
 * 
 */
public class UriTemplate {
	
	// Fields

	private String uriTemplate;
	private MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();

	// Constructors

	public UriTemplate(String uriTemplate) {
		Assert.hasText(uriTemplate, "'uriTemplate' must not be empty");

		this.uriTemplate = uriTemplate;
	}

	// Implementations

	public UriTemplate tmpl() {
		UriTemplate uriTemplate = new UriTemplate(this.uriTemplate);
		uriTemplate.params.putAll(this.params);
		return uriTemplate;
	}

	public UriTemplate param(String name, Object... values) {
		Assert.hasText(name, "'name' must not be empty");

		if (values == null || values.length == 0) {
			this.params.put(name, null);
		}
		else if (values.length > 1) {
			for (Object value : values) {
				this.params.add(name, value);
			}
		}
		else if (values[0] == null) {
			this.params.add(name, null);
		}
		else if (values[0].getClass().isArray()) {
			int length = Array.getLength(values[0]);
			for (int i = 0; i < length; i++) {
				this.params.add(name, Array.get(values[0], i));
			}
		}
		else if (values[0] instanceof Collection) {
			for (Object value : (Collection<?>) values[0]) {
				this.params.add(name, value);
			}
		}
		else {
			this.params.add(name, values[0]);
		}
		return this;
	}

	public UriTemplate params(String... names) {
		Assert.notNull(names, "'names' must not be null");

		for (String name : names) {
			param(name);
		}
		return this;
	}

	public UriTemplate params(Map<String, ?> params) {
		Assert.notNull(params, "'params' must not be null");

		for (Entry<String, ?> param : params.entrySet()) {
			param(param.getKey(), param.getValue());
		}
		return this;
	}

	public UriComponents build() { return build(false); }

	public UriComponents build(HttpServletRequest request) { return build(false, request); }

	public UriComponents build(boolean encoded) { return build(encoded, WebUtils.getCurrentRequest()); }

	public UriComponents build(boolean encoded, HttpServletRequest request) {
		Assert.notNull(request, "'request' is null");

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uriTemplate);

		for (Entry<String, List<Object>> entry : params.entrySet()) {
			String name = entry.getKey();
			List<Object> values = entry.getValue();

			if (values == null) {
				Object[] paramValues = request.getParameterValues(name);
				if (paramValues != null) {
					uriBuilder.queryParam(name, paramValues);
				}
			} else {
				uriBuilder.queryParam(name, values.toArray());
			}
		}

		return uriBuilder.build(encoded);
	}

	public String toUriString() { return build().encode().toUriString(); }
	public String toUriString(Object... uriVariableValues) { return build().expand(uriVariableValues).encode().toUriString(); }
	public String toUriString(Map<String,?> uriVariableValues) { return build().expand(uriVariableValues).encode().toUriString(); }

	@Override public String toString() { return toUriString(); }
}
