package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.TopicVoteDTO;
import sba301.java.opentalk.entity.TopicPoll;
import sba301.java.opentalk.entity.TopicVote;

@Mapper(uses = {UserMapper.class})
public interface TopicVoteMapper {
    TopicVoteMapper INSTANCE = Mappers.getMapper(TopicVoteMapper.class);

    @Mapping(target = "topicPoll", source = "topicPollId", qualifiedByName = "pollFromId")
    TopicVote toEntity(TopicVoteDTO dto);

    @Mapping(target = "topicPollId", source = "topicPoll.id")
    TopicVoteDTO toDto(TopicVote entity);

    @Named("pollFromId")
    default TopicPoll pollFromId(Long id) {
        if (id == null) return null;
        TopicPoll p = new TopicPoll();
        p.setId(id);
        return p;
    }
}
