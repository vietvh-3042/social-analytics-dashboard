package com.learning.social_analytics_dashboard.integration.containers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.wait.strategy.Wait;

@Testcontainers
class InfrastructureContainersTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
            .withDatabaseName("social_analytics")
            .withUsername("social_analytics")
            .withPassword("test-password");

    @Container
    static GenericContainer<?> artemis = new GenericContainer<>("apache/activemq-artemis:2.37.0-alpine")
            .withEnv("ARTEMIS_USER", "artemis")
            .withEnv("ARTEMIS_PASSWORD", "test-password")
            .withExposedPorts(61616)
            .waitingFor(Wait.forListeningPort());

    @Test
    void postgresAndArtemisAreReachable() {
        assertThat(postgres.isRunning()).isTrue();
        assertThat(artemis.isRunning()).isTrue();
        assertThat(postgres.getJdbcUrl()).startsWith("jdbc:postgresql://");
        assertThat(artemis.getMappedPort(61616)).isPositive();
    }
}
