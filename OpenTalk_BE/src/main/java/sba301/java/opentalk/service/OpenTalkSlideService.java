package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.OpenTalkSlideDto;
import sba301.java.opentalk.exception.AppException;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

public interface OpenTalkSlideService {
    public void saveSlide(OpenTalkSlideDto openTalkSlideDTO, MultipartFile file) throws AppException, ExecutionException, InterruptedException;
}
