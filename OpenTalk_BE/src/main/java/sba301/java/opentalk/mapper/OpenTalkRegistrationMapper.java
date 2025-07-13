package sba301.java.opentalk.mapper;

import sba301.java.opentalk.dto.OpenTalkRegistrationDTO;
import sba301.java.opentalk.entity.OpenTalkRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OpenTalkRegistrationMapper {
    OpenTalkRegistrationMapper INSTANCE = Mappers.getMapper(OpenTalkRegistrationMapper.class);

    @Mapping(source = "user.id", target = "userId")
    OpenTalkRegistrationDTO toDto(OpenTalkRegistration openTalkRegistration);
}
