package sba301.java.opentalk.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.SalaryRequest;
import sba301.java.opentalk.dto.SalaryResponse;
import sba301.java.opentalk.entity.Salary;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.enums.SalaryStatus;
import sba301.java.opentalk.repository.AttendanceRepository;
import sba301.java.opentalk.repository.HostRegistrationRepository;
import sba301.java.opentalk.repository.SalaryRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.SalaryService;
import sba301.java.opentalk.dto.UserHostFrequency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;
    private final HostRegistrationRepository hostRegistrationRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public List<SalaryResponse> calculateSalaries(SalaryRequest request) {
        List<SalaryResponse> responses = new ArrayList<>();

        // B1: Map userId -> approved host count
        Map<Long, Integer> hostMap = hostRegistrationRepository.getUserHostFrequency()
                .stream()
                .collect(Collectors.toMap(
                        UserHostFrequency::getUserId,
                        u -> u.getApprovedCount() != null ? u.getApprovedCount().intValue() : 0
                ));

        // B2: Chọn danh sách user để tính
        List<User> users = (request.getRecipientIds() == null || request.getRecipientIds().isEmpty())
                ? userRepository.findAll()
                : userRepository.findAllById(request.getRecipientIds());

        // B3: Tính từng người
        for (User user : users) {
            Long userId = user.getId();

            Optional<Salary> existing = salaryRepository.findByRecipientIdAndPeriodStartAndPeriodEnd(
                    userId,
                    request.getPeriodStart(),
                    request.getPeriodEnd()
            );
            if (existing.isPresent()) {
                continue;
            }

            int totalHost = hostMap.getOrDefault(userId, 0);
            int totalAttendance = attendanceRepository.countAttendanceByUserIdAndCreatedAtBetween(
                    userId,
                    request.getPeriodStart().atStartOfDay(),
                    request.getPeriodEnd().plusDays(1).atStartOfDay()
            );

            BigDecimal baseSalary = BigDecimal.valueOf(50000);
            BigDecimal bonus = baseSalary.multiply(BigDecimal.valueOf(totalHost));

            Salary salary = Salary.builder()
                    .recipient(user)
                    .periodStart(request.getPeriodStart())
                    .periodEnd(request.getPeriodEnd())
                    .totalHostSessions(totalHost)
                    .totalAttendanceSessions(totalAttendance)
                    .baseSalary(baseSalary)
                    .bonus(bonus)
                    .totalSalary(bonus)
                    .status(SalaryStatus.PENDING)
                    .formulaUsed("hostCount * 50.000 VND")
                    .build();

            salaryRepository.save(salary);
            responses.add(toResponse(salary));
        }

        return responses;
    }


    @Override
    public Page<SalaryResponse> getAll(Long recipientId, LocalDate start, LocalDate end, Pageable pageable) {
        return salaryRepository.search(recipientId, start, end, pageable)
                .map(this::toResponse);
    }

    @Override
    public SalaryResponse getById(Long id) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary not found"));
        return toResponse(salary);
    }

    @Override
    public void delete(Long id) {
        salaryRepository.deleteById(id);
    }

    private SalaryResponse toResponse(Salary s) {
        return SalaryResponse.builder()
                .id(s.getId())
                .recipientName(s.getRecipient().getFullName())
                .recipientEmail(s.getRecipient().getEmail())
                .periodStart(s.getPeriodStart())
                .periodEnd(s.getPeriodEnd())
                .baseSalary(s.getBaseSalary())
                .bonus(s.getBonus())
                .totalSalary(s.getTotalSalary())
                .totalHostSessions(s.getTotalHostSessions())
                .totalAttendanceSessions(s.getTotalAttendanceSessions())
                .formulaUsed(s.getFormulaUsed())
                .status(s.getStatus().name())
                .build();
    }

    @Override
    public Optional<Salary> getExistingSalary(Long recipientId, LocalDate periodStart, LocalDate periodEnd) {
        return salaryRepository.findByRecipientIdAndPeriodStartAndPeriodEnd(recipientId, periodStart, periodEnd);
    }

    public Salary createSalaryIfNotExists(Long recipientId, LocalDate start, LocalDate end) {
        Optional<Salary> existing = salaryRepository.findByRecipientIdAndPeriodStartAndPeriodEnd(recipientId, start, end);

        if (existing.isPresent()) {
            return existing.get();
        }

        Salary newSalary = new Salary();
        newSalary.setRecipient(userRepository.findById(recipientId).orElseThrow());
        newSalary.setPeriodStart(start);
        newSalary.setPeriodEnd(end);
        // tính toán và set các thông số khác như bonus, totalSalary...

        return salaryRepository.save(newSalary);
    }

    @Override
    public void updateStatus(Long salaryId, SalaryStatus status) {
        Salary salary = salaryRepository.findById(salaryId)
                .orElseThrow(() -> new RuntimeException("Salary not found"));
        salary.setStatus(status);
        salaryRepository.save(salary);
    }

}