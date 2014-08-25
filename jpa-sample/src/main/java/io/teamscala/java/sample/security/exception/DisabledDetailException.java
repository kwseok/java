package io.teamscala.java.sample.security.exception;

import org.springframework.security.authentication.AccountStatusException;

public class DisabledDetailException extends AccountStatusException {
	private static final long serialVersionUID = 4871257140167810303L;

	/** 상세 사유. */
	private String reason;
	
	// Constructors
	
	public DisabledDetailException(String msg) { super(msg); }
	public DisabledDetailException(String msg, String reason) {
		super(msg);
		this.reason = reason;
	}
	
	// Generated Getters and Setters...

	public String getReason() { return reason; }
	public void setReason(String reason) { this.reason = reason; }
}
