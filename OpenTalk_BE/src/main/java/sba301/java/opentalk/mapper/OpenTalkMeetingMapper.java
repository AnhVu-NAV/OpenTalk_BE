package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.entity.CompanyBranch;
import sba301.java.opentalk.entity.OpenTalkMeeting;

@Mapper
public interface OpenTalkMeetingMapper {
    OpenTalkMeetingMapper INSTANCE = Mappers.getMapper(OpenTalkMeetingMapper.class);

    @Mapping(source = "companyBranch.id", target = "companyBranch")
    OpenTalkMeetingDTO toDto(OpenTalkMeeting topic);

    @Mapping(target = "companyBranch", source = "companyBranch", qualifiedByName = "idToCompanyBranch")
    OpenTalkMeeting toEntity(OpenTalkMeetingDTO dto);

    @Named("idToCompanyBranch")
    default CompanyBranch idToCompanyBranch(Long id) {
        if (id == null) return null;
        return new CompanyBranch(id);
    }
}
