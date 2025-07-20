package br.com.dagostini.infrasystem.shared.utils;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(String bucketName, String key, byte[] content, String contentType) {
        boolean bucketExists = s3Client.listBuckets()
                .buckets()
                .stream()
                .anyMatch(b -> b.name().equals(bucketName));

        if (!bucketExists) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        }

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        String policy = "{\n" +
                "  \"Version\": \"2012-10-17\",\n" +
                "  \"Statement\": [\n" +
                "    {\n" +
                "      \"Effect\": \"Allow\",\n" +
                "      \"Principal\": \"*\",\n" +
                "      \"Action\": [\"s3:GetObject\"],\n" +
                "      \"Resource\": [\"arn:aws:s3:::" + bucketName + "/*\"]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        s3Client.putObject(request, RequestBody.fromBytes(content));
        s3Client.putBucketPolicy(b -> b.bucket(bucketName).policy(policy));

        return String.format("http://localhost:9001/%s/%s", bucketName, key);

    }
}
