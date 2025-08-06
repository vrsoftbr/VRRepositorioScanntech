package br.com.vrsoftware.vrreprocessarscanntech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    private final DataBaseProperties properties;

    public DatabaseConfig(DataBaseProperties properties) {
        this.properties = properties;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(DataBaseProperties.POSTGRESQL_DRIVER);
        ds.setUrl(properties.getUrl());
        ds.setUsername(properties.getUsuario());
        ds.setPassword(properties.getSenha());
        return ds;
    }

    @Bean
    public JdbcClient jdbcClient(DataSource dataSource) {
        return JdbcClient.create(dataSource);
    }
}

