package br.com.api;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>("postgres:11.1");

        private static void startContainers() {
            Startables.deepStart(Stream.of(postgreSQL)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                "spring.datasource.url", postgreSQL.getJdbcUrl(),
                "spring.datasource.username", postgreSQL.getUsername(),
                "spring.datasource.password", postgreSQL.getPassword());
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            startContainers();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            MapPropertySource testContainers = new MapPropertySource("testContainers", (Map) createConnectionConfiguration());

            environment.getPropertySources().addFirst(testContainers);
        }

    }

}
