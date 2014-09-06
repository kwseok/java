package io.teamscala.java.sample.security.web;

/**
 * 인증 결과를 반환하기 위한 모델.
 */
public class AuthenticationResultModel {

    // Fields

    /**
     * 인증 결과.
     */
    private boolean result;

    /**
     * 계정 중지인 경우 별도 메세지.
     */
    private boolean disabled;

    /**
     * 계정 중지에 대한 상세 사유.
     */
    private String reason;

    // Constructors

    public AuthenticationResultModel() {}

    // Generated Getters and Setters...

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
