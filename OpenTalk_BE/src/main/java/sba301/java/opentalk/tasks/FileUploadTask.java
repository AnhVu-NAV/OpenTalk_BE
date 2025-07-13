package sba301.java.opentalk.tasks;

import lombok.RequiredArgsConstructor;
import sba301.java.opentalk.service.FileUploadService;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class FileUploadTask implements Callable<String> {
    private final FileUploadService fileUploadService;
    private final MultipartFile multipartFile;

    @Override
    public String call() throws Exception {
        Thread.sleep(5000);
        return fileUploadService.saveFile(multipartFile);
    }
}
