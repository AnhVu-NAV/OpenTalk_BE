package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.PollDTO;
import sba301.java.opentalk.entity.Poll;

@Mapper(uses = {OpenTalkMeetingMapper.class})
public interface PollMapper {
    PollMapper INSTANCE = Mappers.getMapper(PollMapper.class);

    PollDTO toDto(Poll poll);

    Poll toEntity(PollDTO pollDTO);
}
