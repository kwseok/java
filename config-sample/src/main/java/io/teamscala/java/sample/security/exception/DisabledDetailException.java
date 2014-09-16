package io.teamscala.java.sample.security.exception;

import org.springframework.security.authentication.AccountStatusException;

public class DisabledDetailException extends AccountStatusException {

    /**
     * 상세 사유.
     */
    private String reason;

    // Constructors

    public DisabledDetailException(String msg) {
        super(msg);
    }

    public DisabledDetailException(String msg, String reason) {
        super(msg);
        this.reason = reason;
    }

    // Accessors

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
