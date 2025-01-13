package org.ohorodnik.videostreaming;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import org.ohorodnik.videostreaming.utils.TestConfigurationToCountAllQueries;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE,
        cacheConnection = false)
@Import(TestConfigurationToCountAllQueries.class)
public class BaseContainerImpl {

    private static final PostgreSQLContainer<?> container;

    static {
        container = new PostgreSQLContainer<>("postgres:latest")
                .withUsername("test")
                .withPassword("password")
                .withDatabaseName("videostreaming");

        container.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }
}
