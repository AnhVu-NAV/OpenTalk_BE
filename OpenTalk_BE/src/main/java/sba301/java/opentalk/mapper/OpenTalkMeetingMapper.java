package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.entity.CompanyBranch;
import sba301.java.opentalk.entity.OpenTalkMeeting;

@Mapper(uses = {OpenTalkMeetingMapper.class})
public interface OpenTalkMeetingMapper {
    OpenTalkMeetingMapper INSTANCE = Mappers.getMapper(OpenTalkMeetingMapper.class);

    OpenTalkMeetingDTO toDto(OpenTalkMeeting topic);

    OpenTalkMeeting toEntity(OpenTalkMeetingDTO dto);
}
