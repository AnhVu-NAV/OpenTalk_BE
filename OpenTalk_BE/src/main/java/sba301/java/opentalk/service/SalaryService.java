package sba301.java.opentalk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sba301.java.opentalk.dto.SalaryRequest;
import sba301.java.opentalk.dto.SalaryResponse;
import sba301.java.opentalk.entity.Salary;
import sba301.java.opentalk.enums.SalaryStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalaryService {
    List<SalaryResponse> calculateSalaries(SalaryRequest request);

    Page<SalaryResponse> getAll(Long recipientId, LocalDate start, LocalDate end, Pageable pageable);

    SalaryResponse getById(Long id);

    void delete(Long id);

    Optional<Salary> getExistingSalary(Long recipientId, LocalDate start, LocalDate end);

    Salary createSalaryIfNotExists(Long recipientId, LocalDate start, LocalDate end);

    void updateStatus(Long salaryId, SalaryStatus status);

}
