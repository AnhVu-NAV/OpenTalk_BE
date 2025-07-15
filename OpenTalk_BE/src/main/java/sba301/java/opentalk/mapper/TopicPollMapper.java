package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.TopicPollDTO;
import sba301.java.opentalk.entity.Poll;
import sba301.java.opentalk.entity.Topic;
import sba301.java.opentalk.entity.TopicPoll;


@Mapper
public interface TopicPollMapper {
    TopicPollMapper INSTANCE = Mappers.getMapper(TopicPollMapper.class);

    @Mapping(source = "poll.id", target = "poll")
    @Mapping(source = "topic.id", target = "topic")
    TopicPollDTO toDto(TopicPoll topicPoll);

    @Mapping(source = "poll", target = "poll", qualifiedByName = "idToPoll")
    @Mapping(source = "topic", target = "topic", qualifiedByName = "idToTopic")
    TopicPoll toEntity(TopicPollDTO topicPollDTO);

    @Named("idToPoll")
    default Poll idToPoll(long id) {
        if (id <= 0) return null;
        Poll p = new Poll();
        p.setId(id);
        return p;
    }
    @Named("idToTopic")
    default Topic idToTopic(long id) {
        if (id <= 0) return null;
        Topic t = new Topic();
        t.setId(id);
        return t;
    }
}
