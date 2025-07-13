package sba301.java.opentalk.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.HostRegistrationDTO;
import sba301.java.opentalk.entity.HostRegistration;

public interface HostRegistrationMapper {
    HostRegistrationMapper INSTANCE = Mappers.getMapper(HostRegistrationMapper.class);

    @Mapping(source = "user.id", target = "userId")
    HostRegistrationDTO toDto(HostRegistration hostRegistration);
}