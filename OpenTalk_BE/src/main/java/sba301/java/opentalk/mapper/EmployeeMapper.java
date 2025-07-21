package sba301.java.opentalk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sba301.java.opentalk.dto.EmployeeDTO;
import sba301.java.opentalk.entity.Role;
import sba301.java.opentalk.entity.User;

@Mapper(uses = {CompanyBranchMapper.class})
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "user.role.id", target = "role")
    EmployeeDTO toDto(User user);
    @Mapping(source = "role", target = "role")
    User toEntity(EmployeeDTO employeeDTO);

    default Role map(Long roleId) {
        if (roleId == null) {
            return null;
        }
        Role r = new Role();
        r.setId(roleId);
        return r;
    }
}
