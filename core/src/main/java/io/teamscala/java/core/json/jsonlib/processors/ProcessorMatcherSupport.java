package io.teamscala.java.core.json.jsonlib.processors;

import java.util.Set;

/**
 * Processor matcher support.
 * 
 * @author 석기원
 */
public class ProcessorMatcherSupport {

	public Object getMatch(Class<?> target, Set<?> matches) {
		if (target != null && matches != null) {
			if (matches.contains(target))
                return target;

			for (Object match : matches) {
				if (((Class<?>) match).isAssignableFrom(target))
                    return match;
            }
		}
		return null;
	}
}
