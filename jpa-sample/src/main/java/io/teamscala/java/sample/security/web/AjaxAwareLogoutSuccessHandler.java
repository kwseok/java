package io.teamscala.java.sample.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.teamscala.java.core.web.util.WebUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAwareLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {

    private static final String CONTENT_TYPE = "application/json";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (WebUtils.isAjax(request)) {
            response.setContentType(CONTENT_TYPE);
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            AuthenticationResultModel model = new AuthenticationResultModel();
            model.setResult(true);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), model);
        }
        else {
            super.handle(request, response, authentication);
        }
    }
}
