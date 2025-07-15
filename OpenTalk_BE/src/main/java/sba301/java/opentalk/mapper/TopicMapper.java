package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.entity.Topic;

@Mapper(uses = {UserMapper.class})
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    TopicDTO toDto(Topic topic);

    Topic toEntity(TopicDTO dto);
}
