package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.FeedbackDTO;
import sba301.java.opentalk.entity.Feedback;
import sba301.java.opentalk.entity.OpenTalkMeeting;

@Mapper(uses = {OpenTalkMeetingMapper.class, UserMapper.class})
public interface FeedbackMapper {
    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    FeedbackDTO toFeedbackDTO(Feedback feedback);

    Feedback toFeedback(FeedbackDTO feedbackDTO);
}
