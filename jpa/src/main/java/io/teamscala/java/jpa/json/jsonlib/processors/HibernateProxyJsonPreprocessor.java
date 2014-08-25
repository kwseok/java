package io.teamscala.java.jpa.json.jsonlib.processors;

import io.teamscala.java.core.json.jsonlib.processors.JsonPreprocessor;
import net.sf.json.JsonConfig;
import org.hibernate.proxy.HibernateProxy;

public class HibernateProxyJsonPreprocessor implements JsonPreprocessor<HibernateProxy> {

	@Override
	public Object process(HibernateProxy target, JsonConfig jsonConfig) {
		return processor.processBean(target, jsonConfig);
	}

	private HibernateProxyJsonBeanProcessor processor = new HibernateProxyJsonBeanProcessor();
}
