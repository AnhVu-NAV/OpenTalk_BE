package sba301.java.opentalk.exception;

import lombok.Getter;

@Getter
public class AppException extends Exception {
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
