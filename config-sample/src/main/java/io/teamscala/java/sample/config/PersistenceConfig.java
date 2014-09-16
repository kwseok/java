package io.teamscala.java.sample.config;

import io.teamscala.java.jpa.DefaultEntityManagerContext;
import io.teamscala.java.jpa.JpaHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
public class PersistenceConfig {

    @Inject
    private DataSource dataSource;

    @Inject
    private LoadTimeWeaver loadTimeWeaver;

    @Bean
    public JpaHelper jpaHelper() {
        return new JpaHelper();
    }

    @Bean
    public DefaultEntityManagerContext defaultEntityManagerContext() {
        return new DefaultEntityManagerContext();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPersistenceUnitName("postgresPersistenceUnit");
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setLoadTimeWeaver(loadTimeWeaver);
        return entityManagerFactory;
    }

}
