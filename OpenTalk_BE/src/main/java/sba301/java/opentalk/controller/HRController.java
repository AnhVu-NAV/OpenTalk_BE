package sba301.java.opentalk.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sba301.java.opentalk.dto.EmployeeDTO;
import sba301.java.opentalk.dto.HRDashboardDTO;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.model.response.EmployeeExportDTO;
import sba301.java.opentalk.service.UserService;

import java.util.List;

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
            @RequestParam(required = false) Long companyBranchId
    ) {
        Page<EmployeeDTO> employees = null;
        employees = userService.findEmployees(email, isEnable, companyBranchId, PageRequest.of(page, size));
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

    @GetMapping("/export/")
    public void exportEmployees(@RequestParam(required = false) Boolean status,
                                @RequestParam(required = false) Long companyBranchId,
                                HttpServletResponse response) throws AppException {
        List<EmployeeExportDTO> resutl = userService.exportEmployeeList(status, companyBranchId, response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<HRDashboardDTO> getDashboard() {
        return ResponseEntity.ok(userService.getDataForDashboard());
    }
}
