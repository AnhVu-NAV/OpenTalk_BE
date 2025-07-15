package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.service.UserService;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
public class HRController {

    private final UserService userService;

    @GetMapping("/employees")
    public ResponseEntity<Page<UserDTO>> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String search
    ) {
        Page<UserDTO> employees = userService.findEmployees(search, PageRequest.of(page, size));
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<UserDTO> getEmployeeById(@PathVariable Long id) throws AppException {
        UserDTO employee = userService.getUserById(id);
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/employees/{id}")
    public ResponseEntity<UserDTO> updateEmployee(@PathVariable Long id, @RequestBody UserDTO dto) {
        UserDTO updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/employees")
    public ResponseEntity<UserDTO> createEmployee(@RequestBody UserDTO dto) {
        UserDTO created = userService.createUser(dto);
        return ResponseEntity.ok(created);
    }
}
