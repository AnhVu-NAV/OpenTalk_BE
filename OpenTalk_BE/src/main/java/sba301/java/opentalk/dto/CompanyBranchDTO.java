package sba301.java.opentalk.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CompanyBranchDTO extends BaseDTO {
    private String name;

    private int identify(int cardNumber) {
        return cardNumber;
    }
}
