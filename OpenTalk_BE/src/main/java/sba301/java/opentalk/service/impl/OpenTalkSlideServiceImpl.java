package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sba301.java.opentalk.dto.OpenTalkSlideDto;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.mapper.OpenTalkSlideMapper;
import sba301.java.opentalk.repository.OpenTalkSlideRepository;
import sba301.java.opentalk.service.FileUploadService;
import sba301.java.opentalk.service.OpenTalkSlideService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OpenTalkSlideServiceImpl implements OpenTalkSlideService {
    private final OpenTalkSlideRepository openTalkSlideRepository;
    private final FileUploadService fileUploadService;

    public void saveSlide(OpenTalkSlideDto openTalkSlideDTO, MultipartFile file) {
        log.info("Starting slide save process for file: {}", file.getOriginalFilename());

        CompletableFuture.supplyAsync(() -> {
            try {
                return fileUploadService.saveFile(file);
            } catch (AppException e) {
                throw new RuntimeException(e);
            }
        }).thenApply(filePath -> {
            openTalkSlideDTO.setFilePath(filePath);
            return openTalkSlideDTO;
        }).thenAccept(dto -> {
            openTalkSlideRepository.save(OpenTalkSlideMapper.INSTANCE.toEntity(dto));
            log.info("===================*" + Thread.currentThread().getName() +"*===================");
            log.info("Slide saved successfully for file: {}", file.getOriginalFilename());
        }).exceptionally(e -> {
            log.error("Failed to save slide for file {}: {}", file.getOriginalFilename(), e.getMessage());
            return null;
        });

        log.info("Slide save process submitted for file: {}", file.getOriginalFilename());
    }
}


//        if (filePath.isDone()) {
//        try {
//        openTalkSlideDTO.setFilePath(filePath.get());
//        openTalkSlideRepository.save(OpenTalkSlideMapper.INSTANCE.toEntity(openTalkSlideDTO));
//        log.info("Slide saved successfully for file: {}", file.getOriginalFilename());
//        } catch (InterruptedException e) {
//        Thread.currentThread().interrupt();
//                log.error("File upload was interrupted: {}", e.getMessage());
//        } catch (ExecutionException e) {
//Throwable cause = e.getCause();
//                log.error("File upload failed: {}", cause.getMessage());
//        } finally {
//        executorService.shutdown();
//            }
//                    }




/*  Future
    public void saveSlide(OpenTalkSlideDTO openTalkSlideDTO, MultipartFile file) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        log.info("Starting slide save process for file: {}", file.getOriginalFilename());

        FileUploadTask fileUploadTask = new FileUploadTask(fileUploadService, file);
        Future<String> filePath = executorService.submit(fileUploadTask);

        try {
            openTalkSlideDTO.setFilePath(filePath.get());
            openTalkSlideRepository.save(OpenTalkSlideMapper.INSTANCE.toEntity(openTalkSlideDTO));
            log.info("Slide saved successfully for file: {}", file.getOriginalFilename());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("File upload was interrupted: {}", e.getMessage());
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            log.error("File upload failed: {}", cause.getMessage());
        } finally {
            executorService.shutdown();
        }
    }
 */

/*  Completable Future
    public void saveSlide(OpenTalkSlideDTO openTalkSlideDTO, MultipartFile file) {
        log.info("Starting slide save process for file: {}", file.getOriginalFilename());

        CompletableFuture.supplyAsync(() -> {
            try {
                return fileUploadService.saveFile(file);
            } catch (AppException e) {
                throw new RuntimeException(e);
            }
        }).thenApply(filePath -> {
            openTalkSlideDTO.setFilePath(filePath);
            return openTalkSlideDTO;
        }).thenAccept(dto -> {
            openTalkSlideRepository.save(OpenTalkSlideMapper.INSTANCE.toEntity(dto));
            log.info("===================*" + Thread.currentThread().getName() +"*===================");
            log.info("Slide saved successfully for file: {}", file.getOriginalFilename());
        }).exceptionally(e -> {
            log.error("Failed to save slide for file {}: {}", file.getOriginalFilename(), e.getMessage());
            return null;
        });

        log.info("Slide save process submitted for file: {}", file.getOriginalFilename());
    }
 */