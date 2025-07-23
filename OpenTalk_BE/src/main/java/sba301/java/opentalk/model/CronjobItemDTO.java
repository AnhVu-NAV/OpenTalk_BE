package sba301.java.opentalk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CronjobItemDTO {
    private String cronjobKey;
    private String cronjobValue;
}