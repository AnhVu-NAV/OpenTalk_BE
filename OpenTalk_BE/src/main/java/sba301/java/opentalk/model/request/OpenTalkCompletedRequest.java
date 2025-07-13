package sba301.java.opentalk.model.request;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor

public class OpenTalkCompletedRequest {
    private int page = 0;
    private int size = 10;
    private Long companyBranchId;
    private String hostName;
    private Boolean isOrganized;
    private Boolean isEnableOfHost;
    private LocalDate startDate;
    private LocalDate endDate;
}
