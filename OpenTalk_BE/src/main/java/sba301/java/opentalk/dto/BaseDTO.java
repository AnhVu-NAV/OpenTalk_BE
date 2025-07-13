package sba301.java.opentalk.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
public class BaseDTO {
    private long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
