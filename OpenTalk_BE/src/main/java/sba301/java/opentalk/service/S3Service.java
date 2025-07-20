package sba301.java.opentalk.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    String uploadFile(MultipartFile file) throws IOException;

    byte[] downloadFile(String key);

    void deleteFile(String key);
}
