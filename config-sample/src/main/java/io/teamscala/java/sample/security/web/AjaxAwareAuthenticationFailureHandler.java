package io.teamscala.java.sample.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.teamscala.java.sample.security.exception.DisabledDetailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 인증 실패에 대해서 결과를 JSON 으로 반환한다.
 * 
 */
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	private final Logger logger = LoggerFactory.getLogger(AjaxAwareAuthenticationFailureHandler.class);

	// Constants
	
	/**
	 * JSON Content-Type
	 */
	private static final String CONTENT_TYPE = "application/json";
	
	// Override methods

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {

		// 인증 서비스(현재는 DB 인증 서비스) 오류인 경우에는 에러 레벨로 출력하고, 그렇지 않은 경우 디버그 레벨로 출력한다
		if (exception instanceof AuthenticationServiceException) {
			logger.error(exception.getMessage(), exception);
		} else {
			logger.debug(exception.getMessage(), exception);
		}

		// 서비스 중지된 회원사의 경우 사유를 결과 모델에 세팅한다
		AuthenticationResultModel model = new AuthenticationResultModel();
		if (exception instanceof DisabledDetailException) {
			model.setDisabled(true);
			model.setReason(((DisabledDetailException) exception).getReason());
		}

		response.setContentType(CONTENT_TYPE);
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), model);
	}

}
