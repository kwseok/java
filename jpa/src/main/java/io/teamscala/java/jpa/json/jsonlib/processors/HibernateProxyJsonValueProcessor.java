package io.teamscala.java.jpa.json.jsonlib.processors;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import org.hibernate.proxy.HibernateProxy;

/**
 * Json value processor for {@link HibernateProxy}.
 *
 * @author 석기원
 */
public class HibernateProxyJsonValueProcessor implements JsonValueProcessor {

	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return processor.processBean(value, jsonConfig);
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return processor.processBean(value, jsonConfig);
	}

	private HibernateProxyJsonBeanProcessor processor = new HibernateProxyJsonBeanProcessor();
}