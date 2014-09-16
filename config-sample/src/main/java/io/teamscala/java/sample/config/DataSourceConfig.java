package io.teamscala.java.sample.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.typesafe.config.Config;
import io.teamscala.java.sample.misc.Configs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

@Configuration
public class DataSourceConfig {

    @Bean(destroyMethod = "close")
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        Config dbConfig = Configs.glas.getConfig("database");
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(dbConfig.getString("driverClass"));
        dataSource.setJdbcUrl(dbConfig.getString("jdbcUrl"));
        dataSource.setUser(dbConfig.getString("user"));
        dataSource.setPassword(dbConfig.getString("password"));
        dataSource.setMaxPoolSize(dbConfig.getInt("maxPoolSize"));
        dataSource.setMinPoolSize(dbConfig.getInt("minPoolSize"));
        dataSource.setMaxStatements(dbConfig.getInt("maxStatements"));
        dataSource.setTestConnectionOnCheckout(dbConfig.getBoolean("testConnectionOnCheckout"));
        return dataSource;
    }

}
