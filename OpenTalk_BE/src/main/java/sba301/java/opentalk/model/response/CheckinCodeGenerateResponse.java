package sba301.java.opentalk.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CheckinCodeGenerateResponse {
    private String checkinCode;
    private LocalDateTime expiresAt;
}
