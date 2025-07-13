package sba301.java.opentalk.service.impl;

import lombok.extern.slf4j.Slf4j;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.exception.ErrorCode;
import sba301.java.opentalk.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String saveFile(MultipartFile file) throws AppException {
        File uploadFolder = new File(uploadDir);

        if (!uploadFolder.exists() && !uploadFolder.mkdir()) {
            log.error("Failed to create upload directory: {}", uploadDir);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        String filePath = uploadFolder.getAbsolutePath() + File.separator + file.getOriginalFilename();
        log.info("Saving file to absolute path: {}", filePath);

        File destinationFile = new File(filePath);

        try {
            file.transferTo(destinationFile);
            Thread.sleep(5000);
            log.info("File successfully uploaded to {}", filePath);
        } catch (IOException e) {
            log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return filePath;
    }
}
