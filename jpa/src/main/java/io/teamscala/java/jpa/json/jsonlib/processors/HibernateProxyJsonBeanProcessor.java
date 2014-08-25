package io.teamscala.java.jpa.json.jsonlib.processors;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

public class HibernateProxyJsonBeanProcessor implements JsonBeanProcessor {

	@Override
	public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
		if (bean instanceof HibernateProxy) {
			LazyInitializer lazyInitializer = ((HibernateProxy) bean).getHibernateLazyInitializer();
			if (!lazyInitializer.isUninitialized()) {
				return JSONObject.fromObject(lazyInitializer.getImplementation(), jsonConfig);
            }
		}
		return new JSONObject(true);
	}
}
