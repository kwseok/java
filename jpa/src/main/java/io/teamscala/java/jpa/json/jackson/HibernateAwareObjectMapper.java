package io.teamscala.java.jpa.json.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;

import java.util.Iterator;
import java.util.List;

/**
 * Hibernate aware object mapper.
 */
public class HibernateAwareObjectMapper extends ObjectMapper {

    private final Hibernate4Module hibernate4Module;

    public HibernateAwareObjectMapper() {
        this(false);
    }

    public HibernateAwareObjectMapper(boolean prettyPrint) {
        this.hibernate4Module = new Hibernate4Module() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
                    @Override
                    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                                     BeanDescription beanDesc,
                                                                     List<BeanPropertyWriter> beanProperties) {

                        if (FieldHandled.class.isAssignableFrom(beanDesc.getBeanClass())) {
                            for (Iterator<BeanPropertyWriter> iter = beanProperties.iterator(); iter.hasNext(); ) {
                                if (FieldHandler.class.isAssignableFrom(iter.next().getPropertyType())) iter.remove();
                            }
                        }
                        return beanProperties;
                    }
                });
            }
        };
        registerModule(hibernate4Module);

        setPrettyPrint(prettyPrint);

        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
    }

    public HibernateAwareObjectMapper setPrettyPrint(boolean prettyPrint) {
        configure(SerializationFeature.INDENT_OUTPUT, prettyPrint);
        return this;
    }

    public HibernateAwareObjectMapper enable(Hibernate4Module.Feature f) {
        hibernate4Module.enable(f);
        return this;
    }

    public HibernateAwareObjectMapper disable(Hibernate4Module.Feature f) {
        hibernate4Module.disable(f);
        return this;
    }

    public final boolean isEnabled(Hibernate4Module.Feature f) {
        return hibernate4Module.isEnabled(f);
    }

    public HibernateAwareObjectMapper configure(Hibernate4Module.Feature f, boolean state) {
        hibernate4Module.configure(f, state);
        return this;
    }
}
