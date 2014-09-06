package io.teamscala.java.core.web.method.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Bind Information.
 */
public class BindInfo implements Serializable {
    private static final long serialVersionUID = -5048891521906443457L;

    private final List<BindDescription> descriptions;
    private final List<Method> preBindEvents;
    private final List<Method> postBindEvents;

    public BindInfo(List<BindDescription> descriptions, List<Method> preBindEvents, List<Method> postBindEvents) {
        this.descriptions = descriptions;
        this.preBindEvents = preBindEvents;
        this.postBindEvents = postBindEvents;
    }

    public List<BindDescription> getDescriptions() {
        return descriptions;
    }

    public List<Method> getPreBindEvents() {
        return preBindEvents;
    }

    public List<Method> getPostBindEvents() {
        return postBindEvents;
    }
}
