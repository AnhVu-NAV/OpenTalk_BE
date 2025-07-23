package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.HostRegistrationDTO;
import sba301.java.opentalk.entity.HostRegistration;

@Mapper(uses = {OpenTalkMeetingMapper.class, UserMapper.class})
public interface HostRegistrationMapper {
    HostRegistrationMapper INSTANCE = Mappers.getMapper(HostRegistrationMapper.class);

    HostRegistrationDTO toDto(HostRegistration hostRegistration);
}