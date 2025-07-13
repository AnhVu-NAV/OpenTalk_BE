package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import sba301.java.opentalk.dto.OpenTalkSlideDto;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.service.OpenTalkSlideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/files")
public class FileUploadController {
    private final OpenTalkSlideService slideService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadSlide(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") OpenTalkSlideDto slideDTO) throws AppException, ExecutionException, InterruptedException {
        slideService.saveSlide(slideDTO, file);
        return ResponseEntity.ok("Slide uploaded and saved successfully.");
    }
}
