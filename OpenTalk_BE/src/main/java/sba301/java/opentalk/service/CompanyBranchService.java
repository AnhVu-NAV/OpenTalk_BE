package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.BranchEmployeeCountDTO;
import sba301.java.opentalk.dto.CompanyBranchDTO;
import sba301.java.opentalk.entity.CompanyBranch;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.exception.AppException;

import java.util.List;
import java.util.Optional;

public interface CompanyBranchService {
    List<CompanyBranchDTO> getCompanyBranches();

    CompanyBranchDTO createCompanyBranch(CompanyBranchDTO companyBranch);

    Optional<CompanyBranch> findById(Long id);

    CompanyBranchDTO getCompanyBranchById(Long id) throws AppException;

    CompanyBranchDTO updateCompanyBranch(Long companyBranchId, CompanyBranchDTO companyBranchDTO);

    boolean deleteCompanyBranch(Long companyBranchId);

    List<BranchEmployeeCountDTO> getAllBranchesWithEmployeeCount();

    void notifyAllEmployee(List<User> users);
}