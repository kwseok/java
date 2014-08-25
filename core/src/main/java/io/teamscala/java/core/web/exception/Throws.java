package io.teamscala.java.core.web.exception;

import org.springframework.context.MessageSourceResolvable;

import static javax.servlet.http.HttpServletResponse.*;
import static org.apache.commons.lang3.ArrayUtils.toArray;

/**
 * Throws.
 * 
 * @author 석기원
 * 
 * @see HttpException
 */
public final class Throws {

    private Throws() {}

    // httpException

    public static HttpException httpException(int statusCode, MessageSourceResolvable msr) {
        return httpException(statusCode, msr, (Throwable) null);
    }

    public static HttpException httpException(int statusCode, MessageSourceResolvable msr, Throwable cause) {
        return httpException(statusCode, msr.getCodes(), msr.getArguments(), msr.getDefaultMessage(), cause);
    }

    public static HttpException httpException(int statusCode, String code) {
        return httpException(statusCode, code, (Throwable) null);
    }

    public static HttpException httpException(int statusCode, String code, Throwable cause) {
        return httpException(statusCode, code, (String) null, cause);
    }

    public static HttpException httpException(int statusCode, String code, String defaultMessage) {
        return httpException(statusCode, code, defaultMessage, (Throwable) null);
    }

    public static HttpException httpException(int statusCode, String code, String defaultMessage, Throwable cause) {
        return httpException(statusCode, code, (Object[]) null, defaultMessage, cause);
    }

    public static HttpException httpException(int statusCode, String code, Object[] arguments) {
        return httpException(statusCode, code, arguments, (Throwable) null);
    }

    public static HttpException httpException(int statusCode, String code, Object[] arguments, Throwable cause) {
        return httpException(statusCode, code, arguments, (String) null, cause);
    }

    public static HttpException httpException(int statusCode, String code, Object[] arguments, String defaultMessage) {
        return httpException(statusCode, code, arguments, defaultMessage, (Throwable) null);
    }

    public static HttpException httpException(int statusCode, String code, Object[] arguments, String defaultMessage, Throwable cause) {
        return httpException(statusCode, toArray(code), arguments, defaultMessage, cause);
    }

    public static HttpException httpException(int statusCode, String[] codes) {
        return httpException(statusCode, codes, (Throwable) null);
    }

    public static HttpException httpException(int statusCode, String[] codes, Throwable cause) {
        return httpException(statusCode, codes, (String) null, cause);
    }

    public static HttpException httpException(int statusCode, String[] codes, String defaultMessage) {
        return httpException(statusCode, codes, defaultMessage, (Throwable) null);
    }

    public static HttpException httpException(int statusCode, String[] codes, String defaultMessage, Throwable cause) {
        return httpException(statusCode, codes, (Object[]) null, defaultMessage, cause);
    }

    public static HttpException httpException(int statusCode, String[] codes, Object[] arguments) {
        return httpException(statusCode, codes, arguments, (Throwable) null);
    }

    public static HttpException httpException(int statusCode, String[] codes, Object[] arguments, Throwable cause) {
        return httpException(statusCode, codes, arguments, (String) null, cause);
    }

    public static HttpException httpException(int statusCode, String[] codes, Object[] arguments, String defaultMessage) {
        return httpException(statusCode, codes, arguments, defaultMessage, (Throwable) null);
    }

    public static HttpException httpException(int statusCode, String[] codes, Object[] arguments, String defaultMessage, Throwable cause) {
        return new HttpException(statusCode, codes, arguments, defaultMessage, cause);
    }

    // notFound : SC_NOT_FOUND

    public static HttpException notFound(MessageSourceResolvable msr) {
        return notFound(msr, (Throwable) null);
    }

    public static HttpException notFound(MessageSourceResolvable msr, Throwable cause) {
        return notFound(msr.getCodes(), msr.getArguments(), msr.getDefaultMessage(), cause);
    }

    public static HttpException notFound(String code) {
        return notFound(code, (Throwable) null);
    }

    public static HttpException notFound(String code, Throwable cause) {
        return notFound(code, (String) null, cause);
    }

    public static HttpException notFound(String code, String defaultMessage) {
        return notFound(code, defaultMessage, (Throwable) null);
    }

    public static HttpException notFound(String code, String defaultMessage, Throwable cause) {
        return notFound(code, (Object[]) null, defaultMessage, cause);
    }

    public static HttpException notFound(String code, Object[] arguments) {
        return notFound(code, arguments, (Throwable) null);
    }

    public static HttpException notFound(String code, Object[] arguments, Throwable cause) {
        return notFound(code, arguments, (String) null, cause);
    }

    public static HttpException notFound(String code, Object[] arguments, String defaultMessage) {
        return notFound(code, arguments, defaultMessage, (Throwable) null);
    }

    public static HttpException notFound(String code, Object[] arguments, String defaultMessage, Throwable cause) {
        return notFound(toArray(code), arguments, defaultMessage, cause);
    }

    public static HttpException notFound(String[] codes) {
        return notFound(codes, (Throwable) null);
    }

    public static HttpException notFound(String[] codes, Throwable cause) {
        return notFound(codes, (String) null, cause);
    }

    public static HttpException notFound(String[] codes, String defaultMessage) {
        return notFound(codes, defaultMessage, (Throwable) null);
    }

    public static HttpException notFound(String[] codes, String defaultMessage, Throwable cause) {
        return notFound(codes, (Object[]) null, defaultMessage, cause);
    }

    public static HttpException notFound(String[] codes, Object[] arguments) {
        return notFound(codes, arguments, (Throwable) null);
    }

    public static HttpException notFound(String[] codes, Object[] arguments, Throwable cause) {
        return notFound(codes, arguments, (String) null, cause);
    }

    public static HttpException notFound(String[] codes, Object[] arguments, String defaultMessage) {
        return notFound(codes, arguments, defaultMessage, (Throwable) null);
    }

    public static HttpException notFound(String[] codes, Object[] arguments, String defaultMessage, Throwable cause) {
        return httpException(SC_NOT_FOUND, codes, arguments, defaultMessage, cause);
    }

    // badRequest : SC_BAD_REQUEST

    public static HttpException badRequest(MessageSourceResolvable msr) {
        return badRequest(msr, (Throwable) null);
    }

    public static HttpException badRequest(MessageSourceResolvable msr, Throwable cause) {
        return badRequest(msr.getCodes(), msr.getArguments(), msr.getDefaultMessage(), cause);
    }

    public static HttpException badRequest(String code) {
        return badRequest(code, (Throwable) null);
    }

    public static HttpException badRequest(String code, Throwable cause) {
        return badRequest(code, (String) null, cause);
    }

    public static HttpException badRequest(String code, String defaultMessage) {
        return badRequest(code, defaultMessage, (Throwable) null);
    }

    public static HttpException badRequest(String code, String defaultMessage, Throwable cause) {
        return badRequest(code, (Object[]) null, defaultMessage, cause);
    }

    public static HttpException badRequest(String code, Object[] arguments) {
        return badRequest(code, arguments, (Throwable) null);
    }

    public static HttpException badRequest(String code, Object[] arguments, Throwable cause) {
        return badRequest(code, arguments, (String) null, cause);
    }

    public static HttpException badRequest(String code, Object[] arguments, String defaultMessage) {
        return badRequest(code, arguments, defaultMessage, (Throwable) null);
    }

    public static HttpException badRequest(String code, Object[] arguments, String defaultMessage, Throwable cause) {
        return badRequest(toArray(code), arguments, defaultMessage, cause);
    }

    public static HttpException badRequest(String[] codes) {
        return badRequest(codes, (Throwable) null);
    }

    public static HttpException badRequest(String[] codes, Throwable cause) {
        return badRequest(codes, (String) null, cause);
    }

    public static HttpException badRequest(String[] codes, String defaultMessage) {
        return badRequest(codes, defaultMessage, (Throwable) null);
    }

    public static HttpException badRequest(String[] codes, String defaultMessage, Throwable cause) {
        return badRequest(codes, (Object[]) null, defaultMessage, cause);
    }

    public static HttpException badRequest(String[] codes, Object[] arguments) {
        return badRequest(codes, arguments, (Throwable) null);
    }

    public static HttpException badRequest(String[] codes, Object[] arguments, Throwable cause) {
        return badRequest(codes, arguments, (String) null, cause);
    }

    public static HttpException badRequest(String[] codes, Object[] arguments, String defaultMessage) {
        return badRequest(codes, arguments, defaultMessage, (Throwable) null);
    }

    public static HttpException badRequest(String[] codes, Object[] arguments, String defaultMessage, Throwable cause) {
        return httpException(SC_BAD_REQUEST, codes, arguments, defaultMessage, cause);
    }

    // accessDenied : SC_FORBIDDEN

    public static HttpException accessDenied(MessageSourceResolvable msr) {
        return accessDenied(msr, (Throwable) null);
    }

    public static HttpException accessDenied(MessageSourceResolvable msr, Throwable cause) {
        return accessDenied(msr.getCodes(), msr.getArguments(), msr.getDefaultMessage(), cause);
    }

    public static HttpException accessDenied(String code) {
        return accessDenied(code, (Throwable) null);
    }

    public static HttpException accessDenied(String code, Throwable cause) {
        return accessDenied(code, (String) null, cause);
    }

    public static HttpException accessDenied(String code, String defaultMessage) {
        return accessDenied(code, defaultMessage, (Throwable) null);
    }

    public static HttpException accessDenied(String code, String defaultMessage, Throwable cause) {
        return accessDenied(code, (Object[]) null, defaultMessage, cause);
    }

    public static HttpException accessDenied(String code, Object[] arguments) {
        return accessDenied(code, arguments, (Throwable) null);
    }

    public static HttpException accessDenied(String code, Object[] arguments, Throwable cause) {
        return accessDenied(code, arguments, (String) null, cause);
    }

    public static HttpException accessDenied(String code, Object[] arguments, String defaultMessage) {
        return accessDenied(code, arguments, defaultMessage, (Throwable) null);
    }

    public static HttpException accessDenied(String code, Object[] arguments, String defaultMessage, Throwable cause) {
        return accessDenied(toArray(code), arguments, defaultMessage, cause);
    }

    public static HttpException accessDenied(String[] codes) {
        return accessDenied(codes, (Throwable) null);
    }

    public static HttpException accessDenied(String[] codes, Throwable cause) {
        return accessDenied(codes, (String) null, cause);
    }

    public static HttpException accessDenied(String[] codes, String defaultMessage) {
        return accessDenied(codes, defaultMessage, (Throwable) null);
    }

    public static HttpException accessDenied(String[] codes, String defaultMessage, Throwable cause) {
        return accessDenied(codes, (Object[]) null, defaultMessage, cause);
    }

    public static HttpException accessDenied(String[] codes, Object[] arguments) {
        return accessDenied(codes, arguments, (Throwable) null);
    }

    public static HttpException accessDenied(String[] codes, Object[] arguments, Throwable cause) {
        return accessDenied(codes, arguments, (String) null, cause);
    }

    public static HttpException accessDenied(String[] codes, Object[] arguments, String defaultMessage) {
        return accessDenied(codes, arguments, defaultMessage, (Throwable) null);
    }

    public static HttpException accessDenied(String[] codes, Object[] arguments, String defaultMessage, Throwable cause) {
        return httpException(SC_FORBIDDEN, codes, arguments, defaultMessage, cause);
    }
}
