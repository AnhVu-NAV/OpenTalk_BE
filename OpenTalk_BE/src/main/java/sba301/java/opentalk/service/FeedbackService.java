package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.FeedbackDTO;

import java.util.List;

public interface FeedbackService {
    FeedbackDTO createFeedback(FeedbackDTO dto);
    boolean updateFeedback(FeedbackDTO dto);
    boolean deleteFeedback(long feedbackId);
    List<FeedbackDTO> getFeedbacksOfMeeting(long meetingId);
}
