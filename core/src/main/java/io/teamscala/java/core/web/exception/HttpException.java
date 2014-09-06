package io.teamscala.java.core.web.exception;

import org.springframework.context.MessageSourceResolvable;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 에러 처리를 위한 예외 클래스
 */
public class HttpException extends RuntimeException implements MessageSourceResolvable {

    // Fields

    private int statusCode;        // Response status code.

    private String[] codes;        // Message codes.
    private Object[] arguments;    // Message arguments.
    private String defaultMessage;    // Default message.

    // Field error messages.
    private Map<String, MessageSourceResolvable> fieldErrors = new HashMap<>();

    // Error data.
    private Map<String, Object> data = new HashMap<>();

    // Constructors

    /**
     * 생성자.
     *
     * @param statusCode     응답 상태 코드
     * @param codes          메시지 코드 배열
     * @param arguments      인수
     * @param defaultMessage 디폴트 메시지
     * @param cause          the cause.
     */
    public HttpException(int statusCode, String[] codes, Object[] arguments, String defaultMessage, Throwable cause) {
        super(defaultMessage, cause);
        this.statusCode = statusCode;
        this.codes = codes;
        this.arguments = arguments;
        this.defaultMessage = (defaultMessage != null) ? defaultMessage : (cause != null) ? cause.getMessage() : null;
    }

    // Public methods.

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String[] getCodes() {
        return codes;
    }

    public HttpException setCodes(String[] codes) {
        this.codes = codes;
        return this;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    public HttpException setArguments(Object[] arguments) {
        this.arguments = arguments;
        return this;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }

    public HttpException setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        return this;
    }

    public Map<String, MessageSourceResolvable> getFieldErrors() {
        return fieldErrors;
    }

    public HttpException addFieldErrors(Map<String, MessageSourceResolvable> fieldErrors) {
        this.fieldErrors.putAll(fieldErrors);
        return this;
    }

    public HttpException addFieldError(String field, MessageSourceResolvable fieldErrors) {
        this.fieldErrors.put(field, fieldErrors);
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public HttpException addData(Map<String, ?> data) {
        this.data.putAll(data);
        return this;
    }

    public HttpException addData(String name, Object value) {
        this.data.put(name, value);
        return this;
    }
}