package io.teamscala.java.sample.security.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 사용자 아이디를 쿠키에 저장하는 핸들러
 * 
 * @author 이영진
 */
public class SaveUsernameAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /** 사용자 아이디 저장 요청 파라미터 */
    static final String REQUEST_PARAM_NAME = "remember_username";

    /** 쿠키 이름 */
    static final String COOKIE_NAME = "_SAVED_ID_";

    /** 쿠키 기본 유효 시간 */
    static final int DEFAULT_MAX_AGE = 60 * 60 * 24 * 30;

    /** 쿠키 유효 기간 */
    private int maxAge = DEFAULT_MAX_AGE;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String remember = request.getParameter(REQUEST_PARAM_NAME);
        if (remember != null) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

            Cookie cookie = new Cookie(COOKIE_NAME, username);
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie(COOKIE_NAME, "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    // Generated Getters and Setters...

    /**
     * 쿠키 유효 기간 세팅
     *
     * @param maxAge 유효기간
     */
    public void setMaxAge(int maxAge) { this.maxAge = maxAge; }
}
