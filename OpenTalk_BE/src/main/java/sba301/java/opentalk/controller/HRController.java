package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.EmployeeDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.service.UserService;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
public class HRController {

    private final UserService userService;

    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeDTO>> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(required = false) Boolean isEnable,
            @RequestParam(defaultValue = "2") int companyBranchId
    ) {
        Page<EmployeeDTO> employees = null;
        if (isEnable == null) {
            employees = userService.findEmployees(email, true, companyBranchId,PageRequest.of(page, size));
        }else{
            employees = userService.findEmployees(email, isEnable, companyBranchId,PageRequest.of(page, size));
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) throws AppException {
        EmployeeDTO employee = userService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO dto) {
        EmployeeDTO updated = userService.updateEmployee(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO dto) {
        EmployeeDTO created = userService.createUser(dto);
        return ResponseEntity.ok(created);
    }
}
