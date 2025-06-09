package com.example.demo.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
@Slf4j
public class FileDownloadController {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName; // "intern-storage"

    /**
     * S3에서 파일 다운로드
     * @param fileKey S3 파일 키 (예: "intern/인증서1.jpg")
     * @return 파일 다운로드 응답
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileKey") String fileKey) {
        try {
            // ✅ 디버깅 정보 출력
            log.info("=== 파일 다운로드 디버깅 ===");
            log.info("받은 파일키: [" + fileKey + "]");
            log.info("파일키 길이: " + fileKey.length());
            log.info("버킷명: [" + bucketName + "]");

            // URL 디코딩된 파일키 확인
            String decodedFileKey = URLDecoder.decode(fileKey, StandardCharsets.UTF_8);
            log.info("디코딩된 파일키: [" + decodedFileKey + "]");

            // ✅ S3에서 파일 존재 여부 먼저 확인
            boolean exists = amazonS3.doesObjectExist(bucketName, fileKey);
            log.info("파일 존재 여부 (원본키): " + exists);

            if (!exists) {
                // 디코딩된 키로도 확인
                boolean existsDecoded = amazonS3.doesObjectExist(bucketName, decodedFileKey);
                log.info("파일 존재 여부 (디코딩키): " + existsDecoded);

                if (existsDecoded) {
                    fileKey = decodedFileKey; // 디코딩된 키 사용
                    log.info("디코딩된 키로 변경: " + fileKey);
                } else {
                    // ✅ 버킷의 모든 객체 리스트 출력 (디버깅용)
                    log.info("=== 버킷 내 파일 목록 ===");
                    amazonS3.listObjects(bucketName).getObjectSummaries()
                            .forEach(obj -> log.info("- " + obj.getKey()));

                    return ResponseEntity.notFound().build();
                }
            }

            // S3에서 객체 가져오기
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileKey);
            S3Object s3Object = amazonS3.getObject(getObjectRequest);

            // 나머지 코드는 동일...
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            ObjectMetadata metadata = s3Object.getObjectMetadata();

            String contentType = metadata.getContentType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            long contentLength = metadata.getContentLength();
            String originalFileName = fileKey.substring(fileKey.lastIndexOf('/') + 1);
            String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(contentLength)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + originalFileName + "\"; filename*=UTF-8''" + encodedFileName)
                    .body(new InputStreamResource(inputStream));

        } catch (AmazonS3Exception e) {
            System.err.println("S3 오류: " + e.getMessage());
            System.err.println("오류 코드: " + e.getErrorCode());
            System.err.println("상태 코드: " + e.getStatusCode());
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
