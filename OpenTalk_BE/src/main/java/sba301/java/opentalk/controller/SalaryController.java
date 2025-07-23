package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.SalaryRequest;
import sba301.java.opentalk.dto.SalaryResponse;
import sba301.java.opentalk.entity.Salary;
import sba301.java.opentalk.enums.SalaryStatus;
import sba301.java.opentalk.service.SalaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/salaries")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    @PostMapping("/calculate")
    public ResponseEntity<List<SalaryResponse>> calculate(@RequestBody SalaryRequest request) {
        return ResponseEntity.ok(salaryService.calculateSalaries(request));
    }

    @GetMapping
    public ResponseEntity<Page<SalaryResponse>> getAll(
            @RequestParam(required = false) Long recipientId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LocalDate now = LocalDate.now();
        if (start == null) start = now.withDayOfMonth(1);
        if (end == null) end = now.withDayOfMonth(now.lengthOfMonth());
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(salaryService.getAll(recipientId, start, end, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(salaryService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salaryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody SalaryStatus status) {
        salaryService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

}
