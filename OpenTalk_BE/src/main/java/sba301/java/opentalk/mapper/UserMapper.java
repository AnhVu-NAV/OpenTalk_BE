package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.entity.Role;
import sba301.java.opentalk.entity.User;

@Mapper(uses = {OpenTalkMeetingMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "user.role.id", target = "role")
    UserDTO userToUserDTO(User user);

    @Mapping(source = "role", target = "role")
    User userDTOToUser(UserDTO dto);

    default Role map(Long id) {
        if (id == null) return null;
        return new Role(id);
    }
}
