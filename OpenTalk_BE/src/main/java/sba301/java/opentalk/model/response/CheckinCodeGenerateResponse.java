package sba301.java.opentalk.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CheckinCodeGenerateResponse {
    private String checkinCode;
    private LocalDateTime expiresAt;
}
