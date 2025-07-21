package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import sba301.java.opentalk.dto.BranchEmployeeCountDTO;
import sba301.java.opentalk.dto.CompanyBranchDTO;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.service.CompanyBranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company-branch")
@RequiredArgsConstructor
public class CompanyBranchController {
    private final CompanyBranchService companyBranchService;

    @GetMapping
    public ResponseEntity<List<CompanyBranchDTO>> getCompanyBranchs() {
        List<CompanyBranchDTO> dtos = companyBranchService.getCompanyBranches();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{companyBranchId}")
    public ResponseEntity<CompanyBranchDTO> getCompanyBranchById(
            @PathVariable("companyBranchId") Long companyBranchId) throws AppException {
        CompanyBranchDTO dto = companyBranchService.getCompanyBranchById(companyBranchId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CompanyBranchDTO> createCompanyBranch(@RequestBody CompanyBranchDTO dto) {
        CompanyBranchDTO createdDto = companyBranchService.createCompanyBranch(dto);
        return ResponseEntity.ok(createdDto);
    }

    @PutMapping("/{companyBranchId}")
    public ResponseEntity<CompanyBranchDTO> updateCompanyBranch(@PathVariable Long companyBranchId, @RequestBody CompanyBranchDTO dto) {
        CompanyBranchDTO updatedDto = companyBranchService.updateCompanyBranch(companyBranchId, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{companyBranchId}")
    public ResponseEntity<CompanyBranchDTO> deleteCompanyBranch(@PathVariable Long companyBranchId) {
        return companyBranchService.deleteCompanyBranch(companyBranchId) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/employee-count")
    public List<BranchEmployeeCountDTO> getBranchesWithEmployeeCount() {
        return companyBranchService.getAllBranchesWithEmployeeCount();
    }
}
