package sba301.java.opentalk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sba301.java.opentalk.dto.EmployeeDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(User user);

    List<UserDTO> getUsers();

    List<UserDTO> getAllAdmin();

    UserDTO getUserById(Long userId) throws AppException;

    EmployeeDTO getEmployeeById(Long employeeId) throws AppException;

    UserDTO updateUser(Long userId, UserDTO dto);

    Optional<UserDTO> getUserByUsername(String username);

    Optional<UserDTO> getUserByUsernameWithNativeQuery(String username);

    Optional<UserDTO> getUserByEmail(String email);

    boolean deleteUser(Long userId);

    Slice<UserDTO> getUnregisteredOpenTalks(OpenTalkCompletedRequest request);

    List<UserDTO> getAvailableUsersTobeHost();

    public void generateRandom();

    Page<EmployeeDTO> findEmployees(String email, Boolean isEnable, int companyBranchId, Pageable pageable);

    EmployeeDTO createUser(EmployeeDTO dto);

    EmployeeDTO updateEmployee(Long userId, EmployeeDTO dto);
}
