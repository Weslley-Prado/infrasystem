package br.com.dagostini.infrasystem.shared.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.lang.reflect.Field;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class S3ConfigTest {

    @InjectMocks
    private S3Config s3Config;

    private S3Client s3Client;

    @BeforeEach
    void setUp() {
        s3Client = s3Config.s3Client();
    }

    @Test
    void testS3ClientCreation() {
        assertNotNull(s3Client, "S3Client should not be null");
    }

    @Test
    void testS3ClientEndpointOverride() {
        S3Client testClient = S3Client.builder()
                .endpointOverride(URI.create("http://minio:9000"))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("minioadmin", "minioadmin")))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();

        assertEquals(s3Client.serviceClientConfiguration().region(),
                testClient.serviceClientConfiguration().region(),
                "Region should match the test client");
    }

    @Test
    void testS3ClientRegion() {
        assertEquals(Region.US_EAST_1, s3Client.serviceClientConfiguration().region(),
                "Region should be US_EAST_1");
    }
}