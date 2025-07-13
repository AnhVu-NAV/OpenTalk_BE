package sba301.java.opentalk.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sba301.java.opentalk.dto.BranchEmployeeCountDTO;
import sba301.java.opentalk.dto.CompanyBranchDTO;
import sba301.java.opentalk.entity.CompanyBranch;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.mapper.CompanyBranchMapper;
import sba301.java.opentalk.repository.CompanyBranchRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.CompanyBranchService;
import sba301.java.opentalk.service.NotificationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyBranchServiceImpl implements CompanyBranchService {
    private final CompanyBranchRepository companyBranchRepository;
    private final NotificationService notificationService;

    @Override
    public List<CompanyBranchDTO> getCompanyBranches() {
        List<CompanyBranch> branches = companyBranchRepository.findAllWithUsers();
        return branches.stream().map(CompanyBranchMapper.INSTANCE::toDto).toList();
    }

    @Override
    public CompanyBranchDTO createCompanyBranch(CompanyBranchDTO companyBranchDTO) {
        return CompanyBranchMapper.INSTANCE.toDto(companyBranchRepository.save( CompanyBranchMapper.INSTANCE.toEntity(companyBranchDTO)));
    }

    @Override
    public Optional<CompanyBranch> findById(Long id) {
        return companyBranchRepository.findById(id);
    }

    @Override
    public Optional<CompanyBranchDTO> getCompanyBranchById(Long id) {
        return companyBranchRepository.findById(id).map(CompanyBranchMapper.INSTANCE::toDto);
    }

    @Override
    public CompanyBranchDTO updateCompanyBranch(Long companyBranchId, CompanyBranchDTO companyBranchDTO) {
        CompanyBranch companyBranch = companyBranchRepository.findById(companyBranchId).get();
        if (companyBranchDTO.getName() != null) {
            companyBranch.setName(companyBranchDTO.getName());
        }
        companyBranchRepository.save(companyBranch);
        List<User> users = companyBranch.getUsers();
        notifyAllEmployee(users);
        return CompanyBranchMapper.INSTANCE.toDto(companyBranch);

    }

    @Override
    @Transactional
    public boolean deleteCompanyBranch(Long companyBranchId) {
        CompanyBranch existingCompanyBranch = companyBranchRepository.findById(companyBranchId).orElse(null);
        if (existingCompanyBranch != null) {
            companyBranchRepository.delete(existingCompanyBranch);
            return true;
        }
        return false;
    }

    @Override
    public List<BranchEmployeeCountDTO> getAllBranchesWithEmployeeCount() {
        return companyBranchRepository.findAllBranchesWithEmployeeCount();
    }

    @Override
    public void notifyAllEmployee(List<User> users) {
        for (User user : users) {
            notificationService.sendNotification(user, "Change information of Company");
        }
    }
}
