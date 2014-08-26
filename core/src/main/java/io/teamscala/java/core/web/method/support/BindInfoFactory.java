package io.teamscala.java.core.web.method.support;

import io.teamscala.java.core.util.StringUtils;
import io.teamscala.java.core.web.method.annotation.Bind;
import io.teamscala.java.core.web.method.annotation.PostBind;
import io.teamscala.java.core.web.method.annotation.PreBind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import static java.util.Collections.unmodifiableList;

/**
 * Bind Description factory.
 *
 */
public final class BindInfoFactory {

	private static final Logger LOG = LoggerFactory.getLogger(BindInfoFactory.class);

	/**
	 * Cache for BindDescription list.
	 */
	private static final Map<Class<?>, BindInfo> CACHE = new WeakHashMap<>();

	private static final String GET_PREFIX = "get";
	private static final String IS_PREFIX = "is";

	/**
	 * Get the list of {@link BindDescription}.
	 * 
	 * @param type the target type
	 * @return the list of {@link BindDescription}
	 */
	public static BindInfo get(Class<?> type) {
		if (CACHE.containsKey(type)) return CACHE.get(type);
		synchronized (CACHE) {
			if (CACHE.containsKey(type)) return CACHE.get(type);

			LOG.info("Creating BindDescription for Class[{}]", type);
			BindInfo bindInfo = createBindInfo(type);
			CACHE.put(type, bindInfo);
			return bindInfo;
		}
	}

	/**
	 * Create bind information.
	 * 
	 * @param type the target type.
	 * @return the created bind description list.
	 */
	private static BindInfo createBindInfo(Class<?> type) {
		List<BindDescription> bindDescriptions = new ArrayList<>();
		List<Method> preBindEvents = new ArrayList<>();
		List<Method> postBindEvents = new ArrayList<>();

		Class<?> acls = type;
		while (acls != null && acls != Object.class) {
			// Find fields.
			for (Field f : acls.getDeclaredFields()) {
				int modifiers = f.getModifiers();
				if (Modifier.isStatic(modifiers)) continue;
				if (Modifier.isFinal(modifiers)) continue;

				String targetName = f.getName();

				Bind bind = f.getAnnotation(Bind.class);
				if (bind != null) {
					bindDescriptions.add(new BindDescription(targetName, f.getType(), bind));
                }

				Bind.List binds = f.getAnnotation(Bind.List.class);
				if (binds != null) {
					for (Bind b : binds.value()) {
						bindDescriptions.add(new BindDescription(targetName, f.getType(), b));
                    }
                }
			}

			// Find methods.
			for (Method m : acls.getDeclaredMethods()) {
				if (m.getParameterTypes().length == 0) {
					if (m.isAnnotationPresent(PreBind.class)) preBindEvents.add(m);
					if (m.isAnnotationPresent(PostBind.class)) postBindEvents.add(m);
				}

				int modifiers = m.getModifiers();
				if (Modifier.isStatic(modifiers)) continue;
				if (!Modifier.isPublic(modifiers)) continue;
				if (!isGetter(m)) continue;

				String targetName = StringUtils.uncapitalize(m.getName().substring(3));

				Bind bind = m.getAnnotation(Bind.class);
				if (bind != null) {
					bindDescriptions.add(new BindDescription(targetName, m.getReturnType(), bind));
                }

				Bind.List binds = m.getAnnotation(Bind.List.class);
				if (binds != null) {
					for (Bind b : binds.value()) {
						bindDescriptions.add(new BindDescription(targetName, m.getReturnType(), b));
                    }
                }
			}

			acls = acls.getSuperclass();
		}

		return new BindInfo(
				unmodifiableList(bindDescriptions),
				unmodifiableList(preBindEvents),
				unmodifiableList(postBindEvents));
	}

    private static boolean isGetter(Method m) {
        return m.getParameterTypes().length <= 0 && (
                m.getName().startsWith(IS_PREFIX) && m.getReturnType().equals(boolean.class) ||
                m.getName().startsWith(GET_PREFIX) && !m.getReturnType().equals(boolean.class)
        );
    }
}
