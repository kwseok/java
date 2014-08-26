package io.teamscala.java.core.oxm.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.javabean.BeanProvider;
import com.thoughtworks.xstream.converters.javabean.JavaBeanConverter;

import javax.xml.bind.annotation.XmlTransient;
import java.beans.PropertyDescriptor;

/**
 * XStream marshaller
 *
 */
public class XStreamMarshaller extends org.springframework.oxm.xstream.XStreamMarshaller {

    @Override
    protected void customizeXStream(XStream xstream) {
        xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(), new BeanProvider() {
            @Override
            protected boolean canStreamProperty(PropertyDescriptor descriptor) {
                return super.canStreamProperty(descriptor) &&
                        !descriptor.getReadMethod().isAnnotationPresent(XmlTransient.class);
            }
        }), XStream.PRIORITY_LOW);
    }

}
