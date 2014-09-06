package io.teamscala.java.core.web.servlet.handler;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bind exception handler.
 */
public class BindExceptionHandler implements ExceptionHandler {

    // Fields

    private int statusCode = HttpServletResponse.SC_BAD_REQUEST;
    private Object errorView;

    // Setters

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setErrorViewName(String errorViewName) {
        this.errorView = errorViewName;
    }

    // Implementations for ExceptionHandler

    @Override
    public boolean supports(Exception exception) {
        return (exception instanceof BindException);
    }

    @Override
    public int getStatusCode(Exception exception) {
        return statusCode;
    }

    @Override
    public Object getErrorView(Exception exception) {
        return errorView;
    }

    @Override
    public MessageSourceResolvable getErrorMessage(Exception exception) {
        return ((BindException) exception).getGlobalError();
    }

    @Override
    public Map<String, MessageSourceResolvable> getFieldErrorMessages(Exception exception) {
        List<FieldError> fieldErrors = ((BindException) exception).getFieldErrors();
        Map<String, MessageSourceResolvable> fieldErrorMessages = new HashMap<>(fieldErrors.size());
        for (FieldError fieldError : fieldErrors) {
            fieldErrorMessages.put(fieldError.getField(), fieldError);
        }
        return fieldErrorMessages;
    }

    @Override
    public Map<String, Object> getData(Exception exception) {
        return null;
    }
}
