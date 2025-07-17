package sba301.java.opentalk.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(1005, "USER NOT FOUND", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED(1006, "FILE_UPLOAD_FAILED", HttpStatus.INTERNAL_SERVER_ERROR),
    SLIDE_NOT_FOUND(1007, "SLIDE_NOT_FOUND", HttpStatus.NOT_FOUND),
    VALIDATION_ERROR(1008, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST),
    SYNC_DATA_ERROR(1009, "SYNC_DATA_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    COMPANY_BRANCH_NOT_FOUND(1010, "COMPANY_BRANCH_NOT_FOUND", HttpStatus.NOT_FOUND),;

    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
