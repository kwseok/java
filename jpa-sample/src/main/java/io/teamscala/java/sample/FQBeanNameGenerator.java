package io.teamscala.java.sample;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * FQBN을 빈네임으로 그대로 사용한다.
 *
 */
public class FQBeanNameGenerator implements BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        // 기본 패키지명은 중복되므로 잘라낸다.
        return definition.getBeanClassName().substring(getClass().getPackage().getName().length() + 1);
    }

}
