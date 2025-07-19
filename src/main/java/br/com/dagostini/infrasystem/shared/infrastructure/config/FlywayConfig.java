package br.com.dagostini.infrasystem.shared.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.flywaydb.core.Flyway;

@Configuration
public class FlywayConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    public Flyway flyway(DataSourceProperties properties) {
        Flyway flyway = Flyway.configure()
                .dataSource(properties.getUrl(), properties.getUsername(), properties.getPassword())
                .load();
        flyway.migrate();
        return flyway;
    }
}