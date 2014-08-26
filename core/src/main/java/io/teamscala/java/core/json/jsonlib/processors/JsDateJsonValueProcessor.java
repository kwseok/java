package io.teamscala.java.core.json.jsonlib.processors;

import net.sf.json.JSONNull;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Json value processor for {@link Date}.
 *
 */
public class JsDateJsonValueProcessor implements JsonValueProcessor {

	private final SimpleDateFormat dateTimeProto;

	public JsDateJsonValueProcessor() { this("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); }
	public JsDateJsonValueProcessor(String pattern) {
		this.dateTimeProto = new SimpleDateFormat(pattern); 
		this.dateTimeProto.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override public Object processArrayValue(Object value, JsonConfig jsonConfig) { return process(value, jsonConfig); }
	@Override public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) { return process(value, jsonConfig); }

	private Object process(Object value, JsonConfig jsonConfig) {
		if (value instanceof java.util.Date)
			return dateTimeProto.format(value);

		return JSONNull.getInstance();
	}
}
