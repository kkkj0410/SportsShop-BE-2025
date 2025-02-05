package com.teamproject.back.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

@Component
@Slf4j
public class GcsImage {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String location;

    @Value("${spring.cloud.gcp.storage.origin}")
    private String origin;

    private final Storage storage;

    @Autowired
    public GcsImage(Storage storage) {
        this.storage = storage;
    }


    // 회원 정보 수정
    public String uploadImage(MultipartFile image){

        if(storage == null){
            log.info("Storage 생성 실패");
            return null;
        }

        String uuid = UUID.randomUUID().toString(); // Gcs 에 저장될 파일 이름
        String ext = image.getContentType(); // 파일의 형식 ex) JPG

        // Gcs 이미지 업로드
        BlobId blobId = BlobId.of(bucketName, uuid);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(ext)
                .build();

        try (WriteChannel writer = storage.writer(blobInfo)) {
            byte[] imageData = image.getBytes();
            writer.write(ByteBuffer.wrap(imageData));
        } catch (Exception e) {
            log.error("이미지 전송 실패");
            return null;
        }

        return createURL(uuid);
    }

    public String overWriteImage(MultipartFile newImage, String originalImageUrl){
        if(storage == null){
            log.info("Storage 생성 실패");
            return null;
        }

        String uuid = parseUuid(originalImageUrl);
        String ext = newImage.getContentType();

        // Gcs 이미지 업로드
        BlobId blobId = BlobId.of(bucketName, uuid);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(ext)
                .build();

        try (WriteChannel writer = storage.writer(blobInfo)) {
            byte[] imageData = newImage.getBytes();
            writer.write(ByteBuffer.wrap(imageData));
        } catch (Exception e) {
            log.error("이미지 전송 실패");
            return null;
        }

        return originalImageUrl;
    }

    public boolean deleteImage(String imageUrl){
        String uuid = parseUuid(imageUrl);
        BlobId blobId = BlobId.of(bucketName, uuid);

        if(storage == null){
            log.info("Storage 생성 실패");
            return false;
        }

        if(!storage.delete(blobId)){
            log.info("GCS 이미지 삭제 실패");
            return false;
        }
        log.info("GCS 이미지 삭제 성공");
        return true;
    }

    private String parseUuid(String imageUrl){
        int lastSlashIndex = imageUrl.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            throw new IllegalArgumentException("올바르지 않은 URL 형식입니다.");
        }
        return imageUrl.substring(lastSlashIndex + 1);
    }


    private Storage createStorage(){
        GoogleCredentials credentials = null;
        try{
            credentials = GoogleCredentials.fromStream(new FileInputStream(location));
        } catch (Exception e){
            log.error("이미지 인증 정보 생성 실패");
            return null;
        }

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }

    //ex)
    //https://storage.cloud.google.com/test-image2/3edd7bd0-b60f-4308-a674-b058060c117b
    private String createURL(String uuid){
        return origin + "/" + bucketName + "/" + uuid;
    }
}
