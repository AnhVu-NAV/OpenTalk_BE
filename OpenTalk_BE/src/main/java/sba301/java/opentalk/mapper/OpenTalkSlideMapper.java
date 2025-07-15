package sba301.java.opentalk.mapper;

import sba301.java.opentalk.dto.OpenTalkSlideDto;
import sba301.java.opentalk.entity.OpenTalkSlide;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {OpenTalkMeetingMapper.class, UserMapper.class})
public interface OpenTalkSlideMapper {
    OpenTalkSlideMapper INSTANCE = Mappers.getMapper(OpenTalkSlideMapper.class);

    OpenTalkSlideDto toDto(OpenTalkSlide openTalkSlide);

    OpenTalkSlide toEntity(OpenTalkSlideDto openTalkSlideDTO);
}