package io.teamscala.java.sample.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.teamscala.java.core.web.util.WebUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 인증 성공에 대해서 결과를 JSON 으로 반환한다.
 * 
 */
public class AjaxAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	// Constatnts
	
	/**
	 * JSON Content-Type
	 */
	private static final String CONTENT_TYPE = "application/json";
	
	
	// Override methods

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		if (WebUtils.isAjax(request)) {
			response.setContentType(CONTENT_TYPE);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);

			AuthenticationResultModel model = new AuthenticationResultModel();
			model.setResult(true);

			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), model);
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

}
