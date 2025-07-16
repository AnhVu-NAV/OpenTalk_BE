package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.TopicPollDTO;
import sba301.java.opentalk.entity.TopicPoll;


@Mapper(uses = {PollMapper.class, TopicMapper.class})
public interface TopicPollMapper {
    TopicPollMapper INSTANCE = Mappers.getMapper(TopicPollMapper.class);

    TopicPollDTO toDto(TopicPoll topicPoll);

    TopicPoll toEntity(TopicPollDTO topicPollDTO);
}
