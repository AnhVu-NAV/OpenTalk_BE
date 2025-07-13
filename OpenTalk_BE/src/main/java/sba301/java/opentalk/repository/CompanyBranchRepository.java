package sba301.java.opentalk.repository;

import sba301.java.opentalk.dto.BranchEmployeeCountDTO;
import sba301.java.opentalk.entity.CompanyBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Long> {
    @Query("SELECT new sba301.java.opentalk.dto.BranchEmployeeCountDTO(cb.name, COUNT(u.id)) " +
            "FROM CompanyBranch cb " +
            "JOIN User u ON cb.id = u.companyBranch.id " +
            "GROUP BY cb.name")
    List<BranchEmployeeCountDTO> findAllBranchesWithEmployeeCount();

    @Query("SELECT cb FROM CompanyBranch cb LEFT JOIN cb.users u LEFT JOIN u.role")
    List<CompanyBranch> findAllWithUsers();
}
