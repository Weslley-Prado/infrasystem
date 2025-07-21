package br.com.dagostini.infrasystem.shared.utils;

import br.com.dagostini.infrasystem.shared.utils.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Collections;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3Service s3Service;

    private final String BUCKET_NAME = "test-bucket";
    private final String KEY = "test-file.jpg";
    private final byte[] CONTENT = new byte[]{1, 2, 3};
    private final String CONTENT_TYPE = "image/jpeg";

    @BeforeEach
    void setUp() {
        when(s3Client.listBuckets()).thenReturn(ListBucketsResponse.builder()
                .buckets(Collections.emptyList())
                .build());
    }

    @Test
    void testUploadFile_ConsumerBasedBucketCreation() {
        when(s3Client.listBuckets()).thenReturn(ListBucketsResponse.builder()
                .buckets(Collections.emptyList())
                .build());
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());
        String result = s3Service.uploadFile(BUCKET_NAME, KEY, CONTENT, CONTENT_TYPE);

        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assertEquals("http://localhost:9001/" + BUCKET_NAME + "/" + KEY, result);
    }

    @Test
    void testUploadFile_EmptyBucketName() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            s3Service.uploadFile("dsdsdsd", KEY, null, CONTENT_TYPE);
        });
        assertNotNull(exception);
    }

    @Test
    void testUploadFile_NullKey() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            s3Service.uploadFile(BUCKET_NAME, null, null, CONTENT_TYPE);
        });
        assertNotNull(exception);
    }
}