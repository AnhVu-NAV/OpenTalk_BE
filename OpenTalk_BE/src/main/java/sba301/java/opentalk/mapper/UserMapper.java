package sba301.java.opentalk.mapper;

import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "user.companyBranch.id", target = "companyBranch")
    @Mapping(source = "user.role.id", target = "role")
    UserDTO userToUserDTO(User user);
}
