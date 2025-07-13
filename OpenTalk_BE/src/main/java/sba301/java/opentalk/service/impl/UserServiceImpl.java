package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sba301.java.opentalk.common.RandomOpenTalkNumberGenerator;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.entity.CompanyBranch;
import sba301.java.opentalk.entity.Role;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.exception.ErrorCode;
import sba301.java.opentalk.mapper.UserMapper;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;
import sba301.java.opentalk.repository.CompanyBranchRepository;
import sba301.java.opentalk.repository.RoleRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RandomOpenTalkNumberGenerator randomOpenTalkNumberGenerator;
    private final CompanyBranchRepository companyBranchRepository;

    @Override
    public UserDTO createUser(User user) {
        Optional<Role> role = roleRepository.findById(2L);
        user.setRole(role.get());
        return UserMapper.INSTANCE.userToUserDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> getUsers() {
        log.info("Start get all users query");
//      no use: list, userList
        List<User> users = userRepository.findAll();
        log.info("End get all users query");
        return users.stream().map(UserMapper.INSTANCE::userToUserDTO).toList();
    }

    @Override
    public List<UserDTO> getAllAdmin() {
        return userRepository.findAllByRoleName("ADMIN").stream().map(UserMapper.INSTANCE::userToUserDTO).toList();
    }

    @Cacheable(value = "currentUser", key = "#userId")
    @Override
    public UserDTO getUserById(Long userId) throws AppException {
        log.info("Miss at cache. Call to database.");
        return userRepository.findById(userId)
                .map(UserMapper.INSTANCE::userToUserDTO)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @CachePut(value = "currentUser", key = "#userId")
    @Transactional
    @Override
    public UserDTO updateUser(Long userId, UserDTO dto) {
        User existingUser = userRepository.findById(userId).orElse(null);
        CompanyBranch companyBranch = new CompanyBranch();
        if (existingUser != null) {
            if (dto.getFullName() != null) {
                existingUser.setFullName(dto.getFullName());
            }
            if (dto.getEmail() != null) {
                existingUser.setEmail(dto.getEmail());
            }
            if (dto.getUsername() != null) {
                existingUser.setUsername(dto.getUsername());
            }
            if (dto.getIsEnabled() != null) {
                existingUser.setIsEnabled(dto.getIsEnabled());
            }
            if (dto.getCompanyBranch() != null) {
                companyBranch = companyBranchRepository.findById(dto.getCompanyBranch()).get();
                existingUser.setCompanyBranch(companyBranch);
            }
            if (dto.getUpdatedAt() != null) {
                existingUser.setUpdatedAt(dto.getUpdatedAt());
            }
            companyBranch.setName("Ha Noi");
            log.info("===========================");
            companyBranch.addUser(existingUser);
            log.info("============ Save company branch ===============");
//            companyBranchRepository.save(companyBranch);
//            userRepository.save(existingUser);
            log.info("Updated data at cache.");
            return UserMapper.INSTANCE.userToUserDTO(existingUser);
        }
        return null;
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        log.info("Start HQL query");
        userRepository.findByUsername(username);
        log.info("End HQL query");
        return userRepository.findByUsername(username)
                .map(UserMapper.INSTANCE::userToUserDTO);
    }

    @Override
    public Optional<UserDTO> getUserByUsernameWithNativeQuery(String username) {
        log.info("Start native query");
        Optional<User> user = userRepository.findByUsernameNative(username);
        log.info("End native query");
        return Optional.ofNullable(UserMapper.INSTANCE.userToUserDTO(user.get()));
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper.INSTANCE::userToUserDTO);
    }

    @CacheEvict(value = "currentUser", key = "#userId")
    @Override
    public boolean deleteUser(Long userId) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            userRepository.delete(existingUser);
            log.info("Deleted data at cache.");
            return true;
        }
        return false;
    }

    @Override
    public Slice<UserDTO> getUnregisteredOpenTalks(OpenTalkCompletedRequest request) {
        return userRepository.getUnregisteredEmployees(
                        request.getCompanyBranchId(), request.getHostName(),
                        request.getIsEnableOfHost(),
                        request.getStartDate(), request.getEndDate(),
                        PageRequest.of(request.getPage(), request.getSize()))
                .map(UserMapper.INSTANCE::userToUserDTO);
    }

    @Override
    public List<UserDTO> getAvailableUsersTobeHost() {
//        int currentYear = LocalDate.now().getYear();

        List<User> eligibleUsers = userRepository.findEligibleUsers();
        if (eligibleUsers.isEmpty()) {
            throw new RuntimeException("No eligible users found for hosting OpenTalk!");
        }
        return eligibleUsers.stream().map(UserMapper.INSTANCE::userToUserDTO).toList();
    }

    @Override
    public void generateRandom() {
        System.out.println((int) randomOpenTalkNumberGenerator.generateOpenTalkNumber() * 100);
    }
}
