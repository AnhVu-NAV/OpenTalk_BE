package sba301.java.opentalk.model.request;

import lombok.Data;

@Data
public class CheckinRequest {
    private Long userId;
    private String checkinCode;
}
