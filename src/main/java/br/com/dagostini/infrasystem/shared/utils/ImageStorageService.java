package br.com.dagostini.infrasystem.shared.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageStorageService implements ImageStorageUseCase {

    private static final String BUCKET_NAME = "violations-bucket";
    private static final long MAX_FILE_SIZE = 1048576; // 1MB
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");

    private final S3Client s3Client;

    private final S3Service s3Service;
    @Override
    public String storeImage(MultipartFile file) {
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Picture must be JPEG or PNG");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Picture size exceeds 1MB");
        }

        try {
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            s3Service.uploadFile(
                    BUCKET_NAME,
                    fileName,
                    file.getBytes(),
                    file.getContentType()
            );

            return "http://localhost:9000/" + BUCKET_NAME + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image to S3", e);
        }
    }
}