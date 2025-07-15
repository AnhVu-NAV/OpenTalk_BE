package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.entity.Topic;
import sba301.java.opentalk.entity.User;

@Mapper
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);
    @Mapping(source = "suggestBy.id", target = "suggestBy")
    @Mapping(source = "evaluteBy.id", target = "evaluteBy")
    TopicDTO toDto(Topic topic);

    @Mapping(source = "suggestBy", target = "suggestBy", qualifiedByName = "idToUser")
    @Mapping(source = "evaluteBy", target = "evaluteBy", qualifiedByName = "idToUser")
    Topic toEntity(TopicDTO dto);

    @Named("idToUser")
    default User idToUser(long id) {
        if (id <= 0) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

}
