package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.FeedbackDTO;
import sba301.java.opentalk.entity.Feedback;
import sba301.java.opentalk.mapper.FeedbackMapper;
import sba301.java.opentalk.repository.FeedbackRepository;
import sba301.java.opentalk.service.FeedbackService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Override
    public FeedbackDTO createFeedback(FeedbackDTO dto) {
        return FeedbackMapper.INSTANCE.toFeedbackDTO(feedbackRepository.save(FeedbackMapper.INSTANCE.toFeedback(dto)));
    }

    @Override
    public boolean updateFeedback(FeedbackDTO dto) {
        Feedback feedback = feedbackRepository.findById(dto.getId()).orElse(null);
        if (feedback == null) {
            return false;
        }
        feedback.setComment(dto.getComment());
        feedback.setRate(dto.getRate());
        feedbackRepository.save(feedback);
        return true;
    }

    @Override
    public boolean deleteFeedback(long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback == null) {
            return false;
        }
        feedbackRepository.delete(feedback);
        return true;
    }

    @Override
    public List<FeedbackDTO> getFeedbacksOfMeeting(long meetingId) {
        List<Feedback> feedbacks = feedbackRepository.findAllByMeetingId(meetingId);
        return feedbacks.stream().map(FeedbackMapper.INSTANCE::toFeedbackDTO).collect(Collectors.toList());
    }
}
