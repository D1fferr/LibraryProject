package ua.zakharchuk.ExpectedBooksService.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import ua.zakharchuk.ExpectedBooksService.exceptions.FailedSaveImageException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;
    @Value("${minio.bucket.name}")
    private String bucketName;

    @PostConstruct
    public void initBucket() {
        try {
            s3Client.headBucket(builder -> builder.bucket(bucketName));
        } catch (Exception e) {
            s3Client.createBucket(builder -> builder.bucket(bucketName));
        }
    }
    public String storeImage(MultipartFile imageFile, UUID id){

        try {
            String fileName = generateFileName(imageFile.getOriginalFilename(), id);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(imageFile.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageFile.getBytes()));
            return getImageUrl(fileName);

        }catch (IOException e){
            throw new FailedSaveImageException("Failed to store image file");
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
