package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.FeedbackDTO;
import sba301.java.opentalk.exception.AppException;

import java.util.List;

public interface FeedbackService {
    void createFeedback(FeedbackDTO dto) throws AppException;

    boolean updateFeedback(FeedbackDTO dto);

    boolean deleteFeedback(long feedbackId);

    List<FeedbackDTO> getFeedbacksOfMeeting(long meetingId);
}
