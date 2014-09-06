package io.teamscala.java.core.web.servlet.handler;

import io.teamscala.java.core.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Simple handler exception resolver.
 *
 */
public class SimpleHandlerExceptionResolver implements HandlerExceptionResolver, Ordered, InitializingBean  {

    /** Logger available to subclasses */
    private static final Logger LOG = LoggerFactory.getLogger(SimpleHandlerExceptionResolver.class);

    // Public Constants

    /** The default name of the status code attribute. */
    public static final String DEFAULT_STATUS_CODE_ATTRIBUTE = "statusCode";

    /** The default name of the exception attribute. */
    public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";

    /** The default name of the error message attribute. */
    public static final String DEFAULT_ERROR_MESSAGE_ATTRIBUTE = "errorMessage";

    /** The default name of the field error message attribute. */
    public static final String DEFAULT_FIELD_ERROR_MESSAGES_ATTRIBUTE = "fieldErrorMessages";

    /** The default name of the error data attribute. */
    public static final String DEFAULT_ERROR_DATA_ATTRIBUTE = "data";

    /** The default name of the error view. */
    public static final String DEFAULT_ERROR_VIEW_NAME = "uncaughtException";

    /** The default code of the error. */
    public static final String DEFAULT_ERROR_CODE = "errors.unknown";

    // Private Constants

    private static final String HEADER_PRAGMA = "Pragma";
    private static final String HEADER_EXPIRES = "Expires";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";

    // Fields

    private int order = Ordered.LOWEST_PRECEDENCE;

    private boolean preventResponseCaching = false;

    private String statusCodeAttribute = DEFAULT_STATUS_CODE_ATTRIBUTE;

    private String exceptionAttribute = DEFAULT_EXCEPTION_ATTRIBUTE;

    private String errorMessageAttribute = DEFAULT_ERROR_MESSAGE_ATTRIBUTE;

    private String fieldErrorMessagesAttribute = DEFAULT_FIELD_ERROR_MESSAGES_ATTRIBUTE;

    private String errorDataAttribute = DEFAULT_ERROR_DATA_ATTRIBUTE;

    private Object defaultErrorView = DEFAULT_ERROR_VIEW_NAME;

    private Object ajaxErrorView = null;

    private String defaultErrorCode = DEFAULT_ERROR_CODE;

    private int defaultStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    private List<ExceptionHandler> exceptionHandlers;

    private MessageSource messageSource;

    // Implementations for InitializingBean

    @Override
    public void afterPropertiesSet() throws Exception {
        if (messageSource == null) {
            throw new IllegalArgumentException("'messageSource' is required");
        }
    }

    // Accessors

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Specify whether to prevent HTTP response caching for any view resolved
     * by this HandlerExceptionResolver.
     * <p>Default is "false". Switch this to "true" in order to automatically
     * generate HTTP response headers that suppress response caching.
     *
     * @param preventResponseCaching the prevent response caching.
     */
    public void setPreventResponseCaching(boolean preventResponseCaching) {
        this.preventResponseCaching = preventResponseCaching;
    }

    public void setStatusCodeAttribute(String statusCodeAttribute) {
        this.statusCodeAttribute = statusCodeAttribute;
    }

    public void setExceptionAttribute(String exceptionAttribute) {
        this.exceptionAttribute = exceptionAttribute;
    }

    public void setErrorMessageAttribute(String errorMessageAttribute) {
        this.errorMessageAttribute = errorMessageAttribute;
    }

    public void setFieldErrorMessagesAttribute(String fieldErrorMessagesAttribute) {
        this.fieldErrorMessagesAttribute = fieldErrorMessagesAttribute;
    }

    public void setErrorDataAttribute(String errorDataAttribute) {
        this.errorDataAttribute = errorDataAttribute;
    }

    public void setDefaultErrorViewName(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    public void setDefaultErrorView(View defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    public void setAjaxErrorView(View ajaxErrorView) {
        this.ajaxErrorView = ajaxErrorView;
    }

    public void setAjaxErrorViewName(String ajaxErrorView) {
        this.ajaxErrorView = ajaxErrorView;
    }

    public void setDefaultErrorCode(String defaultErrorCode) {
        this.defaultErrorCode = defaultErrorCode;
    }

    public void setDefaultStatusCode(int defaultStatusCode) {
        this.defaultStatusCode = defaultStatusCode;
    }

    public void setExceptionHandlers(List<ExceptionHandler> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // Implementations for HandlerExceptionResolver

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        prepareResponse(exception, response);

        final Locale locale = RequestContextUtils.getLocale(request);

        int statusCode = 0;
        Object errorView = null;
        String errorMessage = null;
        Map<String, String> fieldErrorMessages = null;
        Map<String, ?> errorData = null;
        boolean resolvedExceptionHandler = false;

        if (this.exceptionHandlers != null) {
            for (ExceptionHandler exceptionHandler : this.exceptionHandlers) {
                if (exceptionHandler.supports(exception)) {
                    resolvedExceptionHandler = true;

                    statusCode = exceptionHandler.getStatusCode(exception);
                    errorView = exceptionHandler.getErrorView(exception);

                    MessageSourceResolvable emr = exceptionHandler.getErrorMessage(exception);
                    if (emr != null) {
                        try {
                            errorMessage = this.messageSource.getMessage(emr, locale);
                        } catch (NoSuchMessageException ignored) {
                            if (emr.getCodes() != null && emr.getCodes().length > 0)
                                errorMessage = "???" + emr.getCodes()[0] + "???";
                            else
                                errorMessage = "??????";
                        }
                    }

                    Map<String, MessageSourceResolvable> femrs = exceptionHandler.getFieldErrorMessages(exception);
                    if (femrs != null) {
                        fieldErrorMessages = new HashMap<>(femrs.size());
                        for (Entry<String, MessageSourceResolvable> entry : femrs.entrySet()) {
                            MessageSourceResolvable femr = entry.getValue();
                            String fem;
                            try {
                                fem = this.messageSource.getMessage(femr, locale);
                            } catch (NoSuchMessageException ignored) {
                                if (femr.getCodes() != null && femr.getCodes().length > 0)
                                    fem = "???" + femr.getCodes()[0] + "???";
                                else
                                    fem = "??????";
                            }
                            fieldErrorMessages.put(entry.getKey(), fem);
                        }
                    }

                    errorData = exceptionHandler.getData(exception);
                    break;
                }
            }
        }

        if (WebUtils.isAjax(request) && this.ajaxErrorView != null)
            errorView = this.ajaxErrorView;
        else if (errorView == null) {
            if (this.defaultErrorView == null) return null;

            errorView = this.defaultErrorView;
        }

        if (statusCode == 0)
            statusCode = this.defaultStatusCode;

        if (!resolvedExceptionHandler)
            errorMessage = this.messageSource
                    .getMessage(this.defaultErrorCode, null,
                            "???" + this.defaultErrorCode + "???",
                            locale);

        LOG.debug("Resolving to status code '" + statusCode + "'");
        LOG.debug("Resolving to error view '" + errorView + "'");

        if (statusCode > 0) response.setStatus(statusCode);

        ModelAndView mav = new ModelAndView();
        if (errorView instanceof View)
            mav.setView((View) errorView);
        else
            mav.setViewName((String) errorView);

        if (this.statusCodeAttribute != null) {
            LOG.debug("Exposing status code as model attribute '" + this.statusCodeAttribute + "'");
            mav.addObject(this.statusCodeAttribute, statusCode);
        }

        if (this.exceptionAttribute != null) {
            LOG.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
            mav.addObject(this.exceptionAttribute, exception);
        }

        if (this.errorMessageAttribute != null) {
            LOG.debug("Exposing error message '" + errorMessage + "' as model attribute '" + this.errorMessageAttribute + "'");
            if (errorMessage != null) mav.addObject(this.errorMessageAttribute, errorMessage);
        }

        if (this.fieldErrorMessagesAttribute != null) {
            LOG.debug("Exposing field error messages '" + fieldErrorMessages + "' as model attribute '" + this.fieldErrorMessagesAttribute + "'");
            if (fieldErrorMessages != null) mav.addObject(this.fieldErrorMessagesAttribute, fieldErrorMessages);
        }

        if (this.errorDataAttribute != null) {
            LOG.debug("Exposing error data '" + errorData + "' as model attribute '" + this.errorDataAttribute + "'");
            if (!errorData.isEmpty()) mav.addObject(this.errorDataAttribute, errorData);
        }

        return mav;
    }

    /**
     * Prepare the response for the exceptional case.
     * <p>The default implementation prevents the response from being cached,
     * if the {@link #setPreventResponseCaching "preventResponseCaching"} property
     * has been set to "true".
     * @param ex the exception that got thrown during handler execution
     * @param response current HTTP response
     * @see #preventCaching
     */
    protected void prepareResponse(Exception ex, HttpServletResponse response) {
        if (this.preventResponseCaching)
            preventCaching(response);
    }

    /**
     * Prevents the response from being cached, through setting corresponding
     * HTTP headers. See <code>http://www.mnot.net/cache_docs</code>.
     * @param response current HTTP response
     */
    protected void preventCaching(HttpServletResponse response) {
        response.addHeader(HEADER_PRAGMA, "no-cache");
        response.addHeader(HEADER_CACHE_CONTROL, "no-cache, no-store, max-age=0");
        response.addDateHeader(HEADER_EXPIRES, 1L);
    }
}
