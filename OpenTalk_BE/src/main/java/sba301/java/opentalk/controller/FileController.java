package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sba301.java.opentalk.entity.Attachment;
import sba301.java.opentalk.repository.AttachmentRepository;
import sba301.java.opentalk.repository.OpenTalkMeetingRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.S3Service;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final S3Service s3Service;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;
    private final OpenTalkMeetingRepository meetingRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("meetingId") Long meetingId
    ) throws IOException {
        String key = s3Service.uploadFile(file);

        Attachment attachment = Attachment.builder()
                .s3Key(key)
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .uploader(userRepository.findById(userId).orElseThrow())
                .openTalkMeeting(meetingRepository.findById(meetingId).orElseThrow())
                .build();

        attachmentRepository.save(attachment);
        return ResponseEntity.ok("File uploaded successfully: " + key);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        byte[] fileData = s3Service.downloadFile(attachment.getS3Key());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(fileData);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        s3Service.deleteFile(attachment.getS3Key());
        attachmentRepository.deleteById(id);

        return ResponseEntity.ok("File deleted successfully");
    }
}

