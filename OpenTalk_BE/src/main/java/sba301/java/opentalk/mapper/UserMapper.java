package sba301.java.opentalk.mapper;

import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.entity.CompanyBranch;
import sba301.java.opentalk.entity.Role;
import sba301.java.opentalk.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "user.role.id", target = "role")
    @Mapping(source = "companyBranch", target = "companyBranch")
    @Mapping(source = "avatarUrl", target = "avatarUrl")
    UserDTO userToUserDTO(User user);

    @Mapping(source = "role", target = "role")
    @Mapping(source = "companyBranch", target = "companyBranch")
    @Mapping(source = "avatarUrl", target = "avatarUrl")
    User userDTOToUser(UserDTO dto);

    default Role map(Long id) {
        if (id == null) return null;
        return new Role(id);
    }

    default CompanyBranch mapBranch(Long id) {
        if (id == null) return null;
        return new CompanyBranch(id);
    }



}
