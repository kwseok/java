package io.teamscala.java.core.web.servlet.handler;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple exception handler.
 */
public class SimpleExceptionHandler implements ExceptionHandler {

    // Fields

    private int statusCode;
    private Object errorView;
    private String[] errorMessageCodes;
    private ArgumentsResolver<Exception> errorMessageArgumentsResolver;
    private String defaultErrorMessage;
    private List<Class<? extends Exception>> exceptions;

    // Constructors

    public SimpleExceptionHandler() {}

    public SimpleExceptionHandler(int statusCode) {
        this.statusCode = statusCode;
    }

    // Setters

    /**
     * Set the status code.
     *
     * @param statusCode the status code.
     * @see HttpServletResponse
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Set the error view.
     *
     * @param errorView {@link View}
     */
    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    /**
     * Set the error view name.
     *
     * @param errorViewName the error view name.
     */
    public void setErrorViewName(String errorViewName) {
        this.errorView = errorViewName;
    }

    /**
     * Set the error message code.
     *
     * @param errorMessageCode the error message code.
     */
    public void setErrorMessageCode(String errorMessageCode) {
        this.errorMessageCodes = new String[]{errorMessageCode};
    }

    /**
     * Set the error message codes.
     *
     * @param errorMessageCodes the error message codes.
     */
    public void setErrorMessageCodes(String[] errorMessageCodes) {
        this.errorMessageCodes = errorMessageCodes;
    }

    /**
     * Set the error message arguments resolver
     *
     * @param argumentsResolver the argument resolver.
     */
    public void setErrorMessageArgumentsResolver(ArgumentsResolver<Exception> argumentsResolver) {
        this.errorMessageArgumentsResolver = argumentsResolver;
    }

    /**
     * Set the error message argument names.
     *
     * @param propertyNames the property names.
     * @see SimpleArgumentsResolver
     */
    public void setErrorMessageArguments(String[] propertyNames) {
        this.errorMessageArgumentsResolver = new SimpleArgumentsResolver<>(propertyNames);
    }

    /**
     * Set the default error message.
     *
     * @param defaultErrorMessage the default error message.
     */
    public void setDefaultErrorMessage(String defaultErrorMessage) {
        this.defaultErrorMessage = defaultErrorMessage;
    }

    /**
     * Set the exception classes.
     *
     * @param exceptions the exceptions.
     */
    public final void setExceptions(List<Class<? extends Exception>> exceptions) {
        this.exceptions = exceptions;
    }

    /**
     * Set the exception class.
     *
     * @param exception the exception.
     */
    public void setException(Class<? extends Exception> exception) {
        if (exception != null) {
            this.exceptions = new ArrayList<>();
            this.exceptions.add(exception);
        } else this.exceptions = null;
    }

    // Implementations for ExceptionHandler

    @Override
    public boolean supports(Exception exception) {
        if (this.exceptions != null) {
            for (Class<?> e : this.exceptions) {
                if (e.isAssignableFrom(exception.getClass())) return true;
            }
        }
        return false;
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
        return new DefaultMessageSourceResolvable(
            this.errorMessageCodes,
            resolveArguments(exception),
            this.defaultErrorMessage);
    }

    @Override
    public Map<String, MessageSourceResolvable> getFieldErrorMessages(Exception exception) {
        return null;
    }

    @Override
    public Map<String, Object> getData(Exception exception) {
        return null;
    }

    protected Object[] resolveArguments(Exception exception) {
        return (this.errorMessageArgumentsResolver != null
            ? this.errorMessageArgumentsResolver.resolve(exception) : null);
    }
}
