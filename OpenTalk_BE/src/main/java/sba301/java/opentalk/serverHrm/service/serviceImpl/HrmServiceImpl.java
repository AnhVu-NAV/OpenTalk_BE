package sba301.java.opentalk.serverHrm.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sba301.java.opentalk.entity.CompanyBranch;
import sba301.java.opentalk.entity.Role;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.exception.ErrorCode;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.serverHrm.model.UserFromHRM;
import sba301.java.opentalk.serverHrm.model.UserHRM;
import sba301.java.opentalk.serverHrm.service.HrmService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HrmServiceImpl implements HrmService {
    private static String DEFAULTPASSWORD = "123";
    @Value("${hrm.api.key}")
    private String HRM_API_KEY;

    private final UserRepository userRepository;
    private final WebClient webClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserFromHRM> findAllUsersFormHRM() {
        return userRepository.findAllUsersFormHRM();
    }

    @Override
    public List<UserHRM> fetchUsersFromHrm() {
        return webClient.get()
                .uri("/users")
                .header("api-key", HRM_API_KEY)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> {
                            log.error("Error fetching users from HRM: {}", clientResponse.statusCode());
                            throw new RuntimeException("Error fetching users from HRM");
                        }
                )
                .bodyToFlux(UserHRM.class)
                .collectList()
                .block();
    }

    @Override
    @Transactional
    @CacheEvict(value = "currentUser", allEntries = true)
    public void syncUsersFromHrm() throws AppException {
        try {
            List<UserHRM> hrmUsers = fetchUsersFromHrm();

            Map<String, User> existingUsers = userRepository.findAll()
                    .stream()
                    .collect(Collectors.toMap(User::getEmail, Function.identity()));

            List<User> usersToSave = new ArrayList<>();

            for (UserHRM hrmUser : hrmUsers) {
                User user = existingUsers.get(hrmUser.getEmail());
                if (user == null) {
                    user = new User();
                    user.setEmail(hrmUser.getEmail());
                    user.setFullName(hrmUser.getFullName());
                    user.setUsername(generateUsername(hrmUser.getEmail(), hrmUser.getFullName()));
                    user.setPassword(passwordEncoder.encode(DEFAULTPASSWORD));
                    user.setCompanyBranch(new CompanyBranch(hrmUser.getCompanyBranch()));
                    user.setRole(new Role(hrmUser.getRole()));
                    user.setCreatedAt(LocalDateTime.now());
                    user.setUpdatedAt(LocalDateTime.now());
                    user.setIsEnabled(hrmUser.getIsEnabled());
                } else {
                    boolean updated = false;
                    if (!user.getFullName().equals(hrmUser.getFullName())) {
                        user.setFullName(hrmUser.getFullName());
                        updated = true;
                    }
                    if (!user.getIsEnabled().equals(hrmUser.getIsEnabled())) {
                        user.setIsEnabled(hrmUser.getIsEnabled());
                        updated = true;
                    }
                    if (updated) {
                        user.setUpdatedAt(LocalDateTime.now());
                    }
                }
                usersToSave.add(user);
            }

            userRepository.saveAll(usersToSave);
        } catch (Exception e) {
            log.error("Error syncing users from HRM", e);
            throw new AppException(ErrorCode.SYNC_DATA_ERROR);
        }
    }


    private String generateUsername(String email, String fullName) {
        if (email != null) {
            return email.split("@")[0];
        }
        return fullName.toLowerCase().replace(" ", ".");
    }
}
