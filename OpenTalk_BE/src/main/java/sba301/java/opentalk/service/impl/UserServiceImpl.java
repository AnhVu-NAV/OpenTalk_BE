package sba301.java.opentalk.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.common.RandomOpenTalkNumberGenerator;
import sba301.java.opentalk.dto.EmployeeDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.entity.CompanyBranch;
import sba301.java.opentalk.entity.Role;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.exception.ErrorCode;
import sba301.java.opentalk.mapper.EmployeeMapper;
import sba301.java.opentalk.mapper.UserMapper;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;
import sba301.java.opentalk.model.response.EmployeeExportDTO;
import sba301.java.opentalk.repository.CompanyBranchRepository;
import sba301.java.opentalk.repository.RoleRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
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
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public List<UserDTO> getAllMeetingManagers() {
        return userRepository.findAllByRoleName("MEETING_MANAGER").stream().map(UserMapper.INSTANCE::userToUserDTO).toList();
    }

    @Override
    public UserDTO getUserById(Long userId) throws AppException {
        log.info("Miss at cache. Call to database.");
        return userRepository.findById(userId)
                .map(UserMapper.INSTANCE::userToUserDTO)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

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
                companyBranch = companyBranchRepository.findById(dto.getCompanyBranch().getId()).get();
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
        userRepository.findByUsername(username);
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

    @Override
    public Page<EmployeeDTO> findEmployees(String email, Boolean isEnable, Long companyBranch, Pageable pageable) {
        Page<User> page = null;
        if (isEnable == null || companyBranch == null) {
            page = userRepository.findByEmailAndIsEnabledAndCompanyBranch(email, null, null, pageable);
        } else {
            CompanyBranch company = companyBranchRepository.findById((long) companyBranch).get();
            page = userRepository.findByEmailAndIsEnabledAndCompanyBranch(email, isEnable, company, pageable);
        }
        return page.map(EmployeeMapper.INSTANCE::toDto);
    }

    @Override
    public EmployeeDTO createUser(EmployeeDTO employeeDTO) {
        // Lấy Role từ roleId trong DTO
        Role role = roleRepository.findById(employeeDTO.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        CompanyBranch branch = null;
        if (employeeDTO.getCompanyBranch() != null) {
            branch = companyBranchRepository.findById(employeeDTO.getCompanyBranch().getId())
                    .orElseThrow(() -> new RuntimeException("CompanyBranch not found"));
        }

        User user = User.builder()
                .fullName(employeeDTO.getFullName())
                .email(employeeDTO.getEmail())
                .username(employeeDTO.getUsername())
                .password(passwordEncoder.encode(employeeDTO.getPassword()))
                .avatarUrl(employeeDTO.getAvatarUrl())
                .isEnabled(employeeDTO.getIsEnabled() != null ? employeeDTO.getIsEnabled() : true)
                .role(role)
                .companyBranch(branch)
                .build();

        User saved = userRepository.save(user);
        return employeeDTO;
    }

    @Override
    public EmployeeDTO updateEmployee(Long userId, EmployeeDTO dto) {
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
                companyBranch = companyBranchRepository.findById(dto.getCompanyBranch().getId()).get();
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
            return EmployeeMapper.INSTANCE.toDto(existingUser);
        }
        return null;
    }

    @Override
    public List<EmployeeExportDTO> exportEmployeeList(Boolean isEnable, Long companyBranchId, HttpServletResponse response) {
        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=employees.xlsx"
        );
        List<User> userEntityList = new ArrayList<>();
        if (isEnable == null || companyBranchId == null) {
            userEntityList = userRepository.findByIsEnabledAndCompanyBranch(true, null);
        } else {
            userEntityList = userRepository.findByIsEnabledAndCompanyBranch(isEnable, companyBranchRepository.findById(companyBranchId).get());
        }
        List<EmployeeExportDTO> employeeExportDTOList = new ArrayList<>();
        for (User userEntity : userEntityList) {
            EmployeeExportDTO employeeExportDTO = mapUsertoEmployeeExportDTO(userEntity);
            employeeExportDTOList.add(employeeExportDTO);
        }
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Employees");
            writeHeader(sheet);
            writeData(sheet, employeeExportDTOList);
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return employeeExportDTOList;
    }

    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) throws AppException {
        return userRepository.findById(employeeId).map(EmployeeMapper.INSTANCE::toDto).orElse(null);
    }

    private void writeHeader(XSSFSheet sheet) {
        XSSFRow header = sheet.createRow(0);
        XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        XSSFFont font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeight(12);
        style.setFont(font);

        String[] cols = {
                "ID", "Full Name", "Email", "Username",
                "Company Branch", "Role", "Enabled",
        };
        for (int i = 0; i < cols.length; i++) {
            XSSFCell cell = header.createCell(i);
            cell.setCellValue(cols[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
    }

    private void writeData(XSSFSheet sheet, List<EmployeeExportDTO> list) {
        int rowNum = 1;
        for (EmployeeExportDTO e : list) {
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(e.getId());
            row.createCell(1).setCellValue(e.getFullName());
            row.createCell(2).setCellValue(e.getEmail());
            row.createCell(3).setCellValue(e.getUsername());
            row.createCell(4).setCellValue(e.getCompanyBranchName());
            row.createCell(5).setCellValue(e.getRoleName());
            row.createCell(6).setCellValue(e.getIsEnabled());
        }
    }

    private EmployeeExportDTO mapUsertoEmployeeExportDTO(User user) {
        if (user == null) {
            return null;
        }
        EmployeeExportDTO dto = new EmployeeExportDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        if (user.getCompanyBranch() != null) {
            dto.setCompanyBranchName(user.getCompanyBranch().getName());
        }
        if (user.getRole() != null) {
            dto.setRoleName(user.getRole().getName());
        }
        dto.setIsEnabled(user.getIsEnabled());
        return dto;
    }

}
