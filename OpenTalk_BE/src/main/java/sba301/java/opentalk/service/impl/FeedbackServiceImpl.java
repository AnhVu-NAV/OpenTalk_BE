package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.FeedbackDTO;
import sba301.java.opentalk.entity.Feedback;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.exception.ErrorCode;
import sba301.java.opentalk.mapper.FeedbackMapper;
import sba301.java.opentalk.repository.FeedbackRepository;
import sba301.java.opentalk.repository.OpenTalkMeetingRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.FeedbackService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final OpenTalkMeetingRepository meetingRepository;

    @Override
    public void createFeedback(FeedbackDTO dto) throws AppException {
        Long userId = dto.getUser().getId();
        Long meetingId = dto.getMeeting().getId();

        boolean exists = feedbackRepository.existsByUserIdAndMeetingId(userId, meetingId);
        if (exists) {
            throw new AppException(ErrorCode.FEEDBACK_ALREADY_SUBMITTED);
        }

        Feedback feedback = FeedbackMapper.INSTANCE.toFeedback(dto);
        feedback.setUser(userRepository.findById(userId).orElseThrow());
        feedback.setMeeting(meetingRepository.findById(meetingId).orElseThrow());
        feedbackRepository.save(feedback);
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
