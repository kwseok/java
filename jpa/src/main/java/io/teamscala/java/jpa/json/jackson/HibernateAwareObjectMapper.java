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
 *
 * @author 석기원
 */
public class HibernateAwareObjectMapper extends ObjectMapper {
	private static final long serialVersionUID = -8608734296572465463L;

	public HibernateAwareObjectMapper() { this(false); }
	public HibernateAwareObjectMapper(boolean prettyPrint) {
        registerModule(new Hibernate4Module() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
                    @Override
                    public List<BeanPropertyWriter> changeProperties(
                            SerializationConfig config,
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
        });

		setPrettyPrint(prettyPrint);

		configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
	}

	public HibernateAwareObjectMapper setPrettyPrint(boolean prettyPrint) {
		configure(SerializationFeature.INDENT_OUTPUT, prettyPrint);
		return this;
	}
}
