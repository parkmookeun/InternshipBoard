package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.entity.PostFile;
import com.example.demo.global.exception.FileDeleteException;
import com.example.demo.global.exception.FileUploadException;
import com.example.demo.global.exception.InvalidFileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${file.max-size:10485760}") // 10MB
    private long maxFileSize;

    // 단일 파일 업로드
    public PostFile uploadFileForPost(MultipartFile file, Post post, Integer fileOrder) throws IOException {

        // 1. 파일 유효성 검사
        validateFile(file);

        // 2. S3에 파일 업로드
        String s3Key = generateS3Key(file.getOriginalFilename());
        String s3Url = uploadToS3(file, s3Key);

        // 3. PostFile 엔티티 생성
        PostFile postFile = new PostFile();
        postFile.setPost(post);
        postFile.setOriginalName(file.getOriginalFilename());
        postFile.setStoredName(extractStoredName(s3Key));
        postFile.setFilePath(s3Url);
        postFile.setFileSize(file.getSize());
        postFile.setMimeType(file.getContentType());
        postFile.setFileOrder(fileOrder != null ? fileOrder : 0);

        return postFile;
    }

    // 여러 파일 업로드
    public List<PostFile> uploadFilesForPost(List<MultipartFile> files, Post post) {
        List<PostFile> uploadedFiles = new ArrayList<>();
        List<String> uploadedS3Keys = new ArrayList<>(); // 롤백용

        try {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (!file.isEmpty()) {
                    PostFile postFile = uploadFileForPost(file, post, i);
                    uploadedFiles.add(postFile);
                }
            }
            return uploadedFiles;
        }catch (IOException e){
            rollbackUploadedFiles(uploadedS3Keys);
            //비즈니스 예외로 변환
            throw new FileUploadException("파일 업로드 중 오류가 발생했습니다:" + e.getMessage(), e);
        }
    }

    // S3에 실제 파일 업로드
    private String uploadToS3(MultipartFile file, String s3Key) throws IOException {
        try {
            // PutObjectRequest 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            // 파일 업로드
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // S3 URL 생성
            String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName, region, s3Key);

            log.info("S3 파일 업로드 완료: {}", s3Url);
            return s3Url;

        } catch (Exception e) {
            log.error("S3 파일 업로드 실패: {}", e.getMessage());
            throw new FileUploadException("파일 업로드에 실패했습니다: " + e.getMessage());
        }
    }

    // S3 키 생성
    private String generateS3Key(String originalFilename) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uniqueFilename = generateUniqueFileName(originalFilename);
        return String.format("intern/%s/%s", datePath, uniqueFilename);
    }

    // 고유 파일명 생성
    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID() + extension;
    }

    // S3 키에서 저장된 파일명 추출
    private String extractStoredName(String s3Key) {
        return s3Key.substring(s3Key.lastIndexOf("/") + 1);
    }

    // 파일 삭제
    public void deleteFile(PostFile postFile) {
        try {
            String s3Key = postFile.getS3Key();
            if (s3Key != null) {
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Key)
                        .build();

                s3Client.deleteObject(deleteRequest);
                log.info("S3 파일 삭제 완료: {}", s3Key);
            }
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", e.getMessage());
        }
    }

    // 파일 유효성 검사
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("파일이 비어있습니다.");
        }

        if (file.getSize() > maxFileSize) {
            throw new InvalidFileException("파일 크기가 너무 큽니다. 최대 " + (maxFileSize / 1024 / 1024) + "MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !isAllowedFileType(contentType)) {
            throw new InvalidFileException("허용되지 않는 파일 형식입니다.");
        }
    }

    // 허용된 파일 타입 검사
    private boolean isAllowedFileType(String contentType) {
        return contentType.startsWith("image/") ||
                contentType.startsWith("video/") ||
                contentType.equals("application/pdf") ||
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }

    private void rollbackUploadedFiles(List<String> s3Keys) {
        for (String s3Key : s3Keys) {
            try {
                deleteFromS3(s3Key);
            } catch (Exception e) {
                log.warn("파일 롤백 실패: {}", s3Key, e);
            }
        }
    }

    // S3에서 파일 삭제 메서드 추가
    public void deleteFromS3(String s3Key) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            s3Client.deleteObject(deleteRequest);
            log.info("S3 파일 삭제 완료: {}", s3Key);

        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", s3Key, e);
            throw new FileDeleteException("S3 파일 삭제에 실패했습니다: " + e.getMessage());
        }
    }
}
