package io.teamscala.java.core.web.servlet.handler;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Exception handler interface.
 */
public interface ExceptionHandler {

    /**
     * 지원 여부.
     *
     * @param exception the exception.
     * @return true or false.
     */
    boolean supports(Exception exception);

    /**
     * 응답 상태 코드.
     *
     * @param exception the exception.
     * @return 응답 상태 코드
     * @see HttpServletResponse
     */
    int getStatusCode(Exception exception);

    /**
     * 에러 뷰.
     *
     * @param exception the exception.
     * @return {@link String} or {@link View}
     */
    Object getErrorView(Exception exception);

    /**
     * 에러 메시지.
     *
     * @param exception the exception.
     * @return {@link MessageSourceResolvable}
     */
    MessageSourceResolvable getErrorMessage(Exception exception);

    /**
     * 필드 에러 메시지 리스트.
     *
     * @param exception the exception.
     * @return {@link Map}
     */
    Map<String, MessageSourceResolvable> getFieldErrorMessages(Exception exception);

    /**
     * 에러 데이타 모델.
     *
     * @param exception the exception.
     * @return {@link Map}
     */
    Map<String, Object> getData(Exception exception);
}
