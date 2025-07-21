package br.com.dagostini.infrasystem.shared.utils;

import br.com.dagostini.infrasystem.shared.utils.ImageStorageService;
import br.com.dagostini.infrasystem.shared.utils.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageStorageServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private ImageStorageService imageStorageService;

    private MockMultipartFile validJpegFile;
    private MockMultipartFile validPngFile;
    private MockMultipartFile invalidFile;
    private MockMultipartFile largeFile;

    private final String BUCKET_NAME = "violations-bucket";
    private final long MAX_FILE_SIZE = 1048576; // 1MB

    @BeforeEach
    void setUp() {
        // Valid JPEG file (500KB)
        validJpegFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new byte[500 * 1024]
        );

        // Valid PNG file (500KB)
        validPngFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                new byte[500 * 1024]
        );

        // Invalid file type
        invalidFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                new byte[500 * 1024]
        );

        // File exceeding size limit (2MB)
        largeFile = new MockMultipartFile(
                "file",
                "large.jpg",
                "image/jpeg",
                new byte[(int) (MAX_FILE_SIZE + 1024)]
        );
    }

    @Test
    void testStoreImage_SuccessfulJpegUpload() throws IOException {
        String expectedUrl = "http://localhost:9000/" + BUCKET_NAME + "/" + System.currentTimeMillis() + "-test.jpg";

        String result = imageStorageService.storeImage(validJpegFile);

        verify(s3Service).uploadFile(
                eq(BUCKET_NAME),
                any(String.class),
                eq(validJpegFile.getBytes()),
                eq("image/jpeg")
        );
        assertTrue(result.startsWith("http://localhost:9000/" + BUCKET_NAME + "/"));
        assertTrue(result.endsWith("-test.jpg"));
    }

    @Test
    void testStoreImage_SuccessfulPngUpload() throws IOException {
        String expectedUrl = "http://localhost:9000/" + BUCKET_NAME + "/" + System.currentTimeMillis() + "-test.png";

        String result = imageStorageService.storeImage(validPngFile);

        verify(s3Service).uploadFile(
                eq(BUCKET_NAME),
                any(String.class),
                eq(validPngFile.getBytes()),
                eq("image/png")
        );
        assertTrue(result.startsWith("http://localhost:9000/" + BUCKET_NAME + "/"));
        assertTrue(result.endsWith("-test.png"));
    }

    @Test
    void testStoreImage_InvalidFileType() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            imageStorageService.storeImage(invalidFile);
        });

        assertEquals("Picture must be JPEG or PNG", exception.getMessage());
        verifyNoInteractions(s3Service);
    }

    @Test
    void testStoreImage_FileSizeExceedsLimit() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            imageStorageService.storeImage(largeFile);
        });

        assertEquals("Picture size exceeds 1MB", exception.getMessage());
        verifyNoInteractions(s3Service);
    }

    @Test
    void testStoreImage_S3UploadFailure() throws IOException {
        doThrow(new RuntimeException("S3 upload failed")).when(s3Service).uploadFile(
                eq(BUCKET_NAME),
                any(String.class),
                any(byte[].class),
                eq("image/jpeg")
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageStorageService.storeImage(validJpegFile);
        });

        assertEquals("Failed to upload image to S3", exception.getMessage());
        verify(s3Service).uploadFile(
                eq(BUCKET_NAME),
                any(String.class),
                eq(validJpegFile.getBytes()),
                eq("image/jpeg")
        );
    }

    @Test
    void testStoreImage_NullFile() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            imageStorageService.storeImage(null);
        });

        assertNotNull(exception);
        verifyNoInteractions(s3Service);
    }

    @Test
    void testStoreImage_EmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.jpg",
                "image/jpeg",
                new byte[0]
        );

        String result = imageStorageService.storeImage(emptyFile);

        verify(s3Service).uploadFile(
                eq(BUCKET_NAME),
                any(String.class),
                eq(new byte[0]),
                eq("image/jpeg")
        );
        assertTrue(result.startsWith("http://localhost:9000/" + BUCKET_NAME + "/"));
        assertTrue(result.endsWith("-empty.jpg"));
    }
}