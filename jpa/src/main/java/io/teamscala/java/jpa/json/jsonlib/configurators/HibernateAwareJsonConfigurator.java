package io.teamscala.java.jpa.json.jsonlib.configurators;

import io.teamscala.java.core.json.jsonlib.configurators.JsonConfigurator;
import io.teamscala.java.jpa.json.jsonlib.filters.HibernateAwarePropertyFilter;
import io.teamscala.java.jpa.json.jsonlib.processors.HibernateProxyJsonBeanProcessor;
import io.teamscala.java.jpa.json.jsonlib.processors.HibernateProxyJsonValueProcessor;
import net.sf.json.JsonConfig;
import org.hibernate.proxy.HibernateProxy;

public class HibernateAwareJsonConfigurator implements JsonConfigurator {

	@Override
	public void configure(JsonConfig jsonConfig) {
		jsonConfig.setJsonPropertyFilter(new HibernateAwarePropertyFilter());
		jsonConfig.registerJsonBeanProcessor(HibernateProxy.class, new HibernateProxyJsonBeanProcessor());
		jsonConfig.registerJsonValueProcessor(HibernateProxy.class, new HibernateProxyJsonValueProcessor());
	}
}
