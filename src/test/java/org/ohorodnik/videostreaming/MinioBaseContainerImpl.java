package org.ohorodnik.videostreaming;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;

import java.time.Duration;

public class MinioBaseContainerImpl extends BaseContainerImpl {

    private static final GenericContainer<?> minioContainer;

    static {
        Integer port = 9000;
        minioContainer = new GenericContainer("minio/minio")
                .withEnv("MINIO_ACCESS_KEY", "admin")
                .withEnv("MINIO_SECRET_KEY", "adminpass")
                .withCommand("server /data")
                .withExposedPorts(port)
                .waitingFor(new HttpWaitStrategy()
                        .forPath("/minio/health/ready")
                        .forPort(port)
                        .withStartupTimeout(Duration.ofSeconds(10)));
        minioContainer.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        Integer mappedPort = minioContainer.getFirstMappedPort();
        org.testcontainers.Testcontainers.exposeHostPorts(mappedPort);
        registry.add("minio.url", () -> String.format("http://%s:%s", minioContainer.getContainerIpAddress(), mappedPort));
        registry.add("minio.username", () -> minioContainer.getEnvMap().get("MINIO_ACCESS_KEY"));
        registry.add("minio.password", () -> minioContainer.getEnvMap().get("MINIO_SECRET_KEY"));
    }
}
