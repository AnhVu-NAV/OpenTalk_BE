package sba301.java.opentalk.mapper;

import sba301.java.opentalk.dto.OpenTalkSlideDto;
import sba301.java.opentalk.entity.OpenTalkSlide;
import sba301.java.opentalk.entity.OpenTalkTopic;
import sba301.java.opentalk.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OpenTalkSlideMapper {
    OpenTalkSlideMapper INSTANCE = Mappers.getMapper(OpenTalkSlideMapper.class);

    @Mapping(source = "openTalkSlide.uploadedBy.id", target = "userId")
    @Mapping(source = "openTalkSlide.openTalkTopic.id", target = "openTalkTopicId")
    OpenTalkSlideDto toDto(OpenTalkSlide openTalkSlide);

    @Mapping(target = "uploadedBy", source = "userId", qualifiedByName = "idToUser")
    @Mapping(target = "openTalkTopic", source = "openTalkTopicId", qualifiedByName = "idToOpenTalkTopic")
    OpenTalkSlide toEntity(OpenTalkSlideDto openTalkSlideDTO);

    @Named("idToUser")
    default User idToUser(Long id) {
        if (id == null) {
            return null;
        }
        return new User(id);
    }

    @Named("idToOpenTalkTopic")
    default OpenTalkTopic idToOpenTalkTopic(Long id) {
        if (id == null) {
            return null;
        }
        return new OpenTalkTopic(id);
    }
}
