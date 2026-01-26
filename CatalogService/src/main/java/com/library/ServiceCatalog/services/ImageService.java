package com.library.ServiceCatalog.services;

import com.library.ServiceCatalog.util.FailedSaveImageException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final S3Client s3Client;
    @Value("${minio.bucket.name}")
    private String bucketName;

    @PostConstruct
    public void initBucket() {
        try {
            log.info("Initialising minio bucket '{}'", bucketName);
            s3Client.headBucket(builder -> builder.bucket(bucketName));
            log.info("Minio bucket initialised '{}'", bucketName);
        } catch (Exception e) {
            s3Client.createBucket(builder -> builder.bucket(bucketName));
            log.info("Minio bucket created '{}'", bucketName);
        }
    }
    public String storeImage(MultipartFile imageFile, UUID id){
        String fileName = generateFileName(imageFile.getOriginalFilename(), id);
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(imageFile.getContentType())
                    .build();
            log.info("Trying to save image: '{}'", fileName);
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageFile.getBytes()));
            log.info("Image saved: '{}'", fileName);
            return getImageUrl(fileName);

        }catch (Exception e){
            log.warn("Failed to save image: '{}'. Cause: '{}'", fileName, e.getMessage());
            throw new FailedSaveImageException("Failed to store image file" + e.getMessage());
        }
    }

    private String getImageUrl(String fileName){
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }
        return s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucketName).key(fileName))
                .toString();
    }

    private String generateFileName(String originalFileName, UUID id){
        String extension = "";
        if (originalFileName != null && originalFileName.contains("."))
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return String.format("books/images/%s%s", id.toString(), extension);
    }

}
