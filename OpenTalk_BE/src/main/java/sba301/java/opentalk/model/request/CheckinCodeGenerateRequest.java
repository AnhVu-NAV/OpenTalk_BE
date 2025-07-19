package sba301.java.opentalk.model.request;

import lombok.Data;

@Data
public class CheckinCodeGenerateRequest {
    private Long meetingId;
    private int validMinutes;
}
