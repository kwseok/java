package io.teamscala.java.sample.web.interceptors;

import io.teamscala.java.sample.security.util.SecurityUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAgentInterceptor extends HandlerInterceptorAdapter {

    public static final String CURRENT_USER_ATRIBUTE_NAME = "CURRENT_USER";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 현재 사용자
        request.setAttribute(CURRENT_USER_ATRIBUTE_NAME, SecurityUtils.getUser());
        return true;
    }
}
