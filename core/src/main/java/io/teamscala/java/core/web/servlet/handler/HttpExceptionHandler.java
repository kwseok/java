package io.teamscala.java.core.web.servlet.handler;

import io.teamscala.java.core.web.exception.HttpException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Http exception handler.
 * 
 */
public class HttpExceptionHandler implements ExceptionHandler {

	// Fields

	private Object defaultErrorView;
	private Map<Integer, Object> errorViewMappings = new HashMap<>();

	// Setters

	public void setDefaultErrorView(View defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	public void setDefaultErrorViewName(String defaultErrorViewName) {
		this.defaultErrorView = defaultErrorViewName;
	}

	public void setErrorViewMappings(Properties errorViewMappings) {
		if (errorViewMappings != null) {
			for (Entry<?, ?> entry : errorViewMappings.entrySet()) {
				String key = String.valueOf(entry.getKey());
				Object value = entry.getValue();
				this.errorViewMappings.put(Integer.parseInt(key), value);
			}
		}
	}

	public void setErrorViewMappings(Map<Integer, ?> errorViewMappings) {
		this.errorViewMappings.putAll(errorViewMappings);
	}

	// Implementations for ExceptionHandler

	@Override
	public boolean supports(Exception exception) {
		return (exception instanceof HttpException);
	}

	@Override
	public int getStatusCode(Exception exception) {
		return ((HttpException) exception).getStatusCode();
	}

	@Override
	public Object getErrorView(Exception exception) {
		int statusCode = getStatusCode(exception);
		if (this.errorViewMappings.containsKey(statusCode))
			return this.errorViewMappings.get(statusCode);

		return defaultErrorView;
	}

	@Override
	public MessageSourceResolvable getErrorMessage(Exception exception) {
		return ((HttpException) exception);
	}

	@Override
	public Map<String, MessageSourceResolvable> getFieldErrorMessages(Exception exception) {
		return ((HttpException) exception).getFieldErrors();
	}

	@Override
	public Map<String, Object> getData(Exception exception) {
		return ((HttpException) exception).getData();
	}
}
