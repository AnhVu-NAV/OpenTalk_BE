package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.model.ApiResponse;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;
import sba301.java.opentalk.serverHrm.service.HrmService;
import sba301.java.opentalk.service.UserService;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final HrmService hrmService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/query/hql/{username}")
    public ResponseEntity<UserDTO> getUsersWithHql(@PathVariable String username) {
        Optional<UserDTO> user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user.get());
    }

    @GetMapping("/query/native/{username}")
    public ResponseEntity<UserDTO> getUsersWithNativeQuery(@PathVariable String username) {
        Optional<UserDTO> user = userService.getUserByUsernameWithNativeQuery(username);
        return ResponseEntity.ok(user.get());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long userId) throws AppException {
        UserDTO user = userService.getUserById(userId);
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .message("User found")
                .result(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/syncData")
    public ResponseEntity<String> synData() throws AppException {
        hrmService.syncUsersFromHrm();
        return ResponseEntity.ok("Sync completed");
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO dto) {
        UserDTO updatedUser = userService.updateUser(userId, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/unregistered")
    public ResponseEntity<Slice<UserDTO>> getUnregisteredOpenTalksEmployees(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Long companyBranchId,
            @RequestParam(required = false) String hostName,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) boolean enableOfHost) {
        OpenTalkCompletedRequest request = new OpenTalkCompletedRequest(
                page, size, companyBranchId, hostName, null, enableOfHost, startDate, endDate);
        Slice<UserDTO> dtos = userService.getUnregisteredOpenTalks(request);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/generateRandomNumber")
    public ResponseEntity<String> generateRandomNumber() {
        userService.generateRandom();
        return ResponseEntity.ok("Random number generated");
    }
}

