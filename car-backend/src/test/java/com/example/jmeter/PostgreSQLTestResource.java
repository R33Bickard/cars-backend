package com.example.jmeter;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.HashMap;
import java.util.Map;

public class PostgreSQLTestResource implements QuarkusTestResourceLifecycleManager {

    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("car_db")
            .withUsername("car_user")
            .withPassword("car_pass");

    @Override
    public Map<String, String> start() {
        postgres.start();

        Map<String, String> config = new HashMap<>();
        config.put("quarkus.datasource.jdbc.url", postgres.getJdbcUrl());
        config.put("quarkus.datasource.username", postgres.getUsername());
        config.put("quarkus.datasource.password", postgres.getPassword());
        return config;
    }

    @Override
    public void stop() {
        postgres.stop();
    }
}
